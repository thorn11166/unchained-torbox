package com.thorn11166.unchainedtorbox.torrentfilepicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thorn11166.unchainedtorbox.data.model.EmptyBodyError
import com.thorn11166.unchainedtorbox.data.model.TorrentItem
import com.thorn11166.unchainedtorbox.data.model.UnchainedNetworkException
import com.thorn11166.unchainedtorbox.data.model.UploadedTorrent
import com.thorn11166.unchainedtorbox.data.repository.TorrentsRepository
import com.thorn11166.unchainedtorbox.torrentdetails.model.TorrentFileItem
import com.thorn11166.unchainedtorbox.utilities.EitherResult
import com.thorn11166.unchainedtorbox.utilities.Event
import com.thorn11166.unchainedtorbox.utilities.Node
import com.thorn11166.unchainedtorbox.utilities.beforeSelectionStatusList
import com.thorn11166.unchainedtorbox.utilities.extension.cancelIfActive
import com.thorn11166.unchainedtorbox.utilities.postEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class TorrentProcessingViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val torrentsRepository: TorrentsRepository,
) : ViewModel() {

    val networkExceptionLiveData = MutableLiveData<Event<UnchainedNetworkException>>()
    val torrentLiveData = MutableLiveData<Event<TorrentEvent>>()
    val structureLiveData = MutableLiveData<Event<Node<TorrentFileItem>>>()

    private var job = Job()

    fun fetchAddedMagnet(magnet: String) {
        viewModelScope.launch {
            val availableHosts = torrentsRepository.getAvailableHosts()
            if (availableHosts.isNullOrEmpty()) {
                Timber.e("Error fetching available hosts")
            } else {
                val addedMagnet = torrentsRepository.addMagnet(magnet, availableHosts.first().host)
                when (addedMagnet) {
                    is EitherResult.Failure -> {
                        Timber.e("Error adding magnet: ${addedMagnet.failure}")
                        networkExceptionLiveData.postEvent(addedMagnet.failure)
                    }
                    is EitherResult.Success -> {
                        setTorrentID(addedMagnet.success.id)
                        torrentLiveData.postEvent(TorrentEvent.Uploaded(addedMagnet.success))
                    }
                }
            }
        }
    }

    fun fetchTorrentDetails(torrentID: String) {

        setTorrentID(torrentID)

        viewModelScope.launch {
            val torrentData: TorrentItem? = torrentsRepository.getTorrentInfo(torrentID)
            // todo: replace using either
            if (torrentData != null) {
                setTorrentDetails(torrentData)
                torrentLiveData.postEvent(TorrentEvent.TorrentInfo(torrentData))
            } else {
                Timber.e("Retrieved torrent info were null for id $torrentID")
            }
        }
    }

    private fun setTorrentDetails(item: TorrentItem) {
        savedStateHandle[KEY_CURRENT_TORRENT] = item
    }

    fun getTorrentID(): String? {
        return savedStateHandle[KEY_CURRENT_TORRENT_ID]
    }

    private fun setTorrentID(id: String) {
        savedStateHandle[KEY_CURRENT_TORRENT_ID] = id
    }

    fun updateTorrentStructure() {
        torrentLiveData.postEvent(TorrentEvent.SelectionUpdated)
    }

    fun startSelectionLoop(files: String = "all") {

        val id = getTorrentID()

        if (id == null) {
            Timber.e("Torrent files selection requested but torrent id was not ready")
            return
        }

        job.cancelIfActive()
        job = Job()

        val scope = CoroutineScope(job + Dispatchers.IO)

        scope.launch {
            var selected = false
            // / maybe job.isActive?
            while (isActive) {
                if (!selected) {
                    when (val selectResponse = torrentsRepository.selectFiles(id, files)) {
                        is EitherResult.Failure -> {
                            if (selectResponse.failure is EmptyBodyError) {
                                Timber.d(
                                    "Select torrent files success returned ${selectResponse.failure.returnCode}"
                                )
                                selected = true
                            } else {
                                Timber.e(
                                    "Exception during torrent files selection call: ${selectResponse.failure}"
                                )
                            }
                        }
                        is EitherResult.Success -> {
                            Timber.d("Select torrent files success")
                            selected = true
                        }
                    }
                }

                if (selected) {
                    val torrentItem: TorrentItem? = torrentsRepository.getTorrentInfo(id)
                    if (torrentItem != null) {
                        if (!beforeSelectionStatusList.contains(torrentItem.status)) {
                            job.cancelIfActive()
                            torrentLiveData.postEvent(TorrentEvent.FilesSelected(torrentItem))
                        }
                    }
                }
                delay(1500)
            }
        }
    }

    fun triggerTorrentEvent(event: TorrentEvent) {
        torrentLiveData.postEvent(event)
    }

    fun fetchUploadedTorrent(binaryTorrent: ByteArray) {
        viewModelScope.launch {
            val availableHosts = torrentsRepository.getAvailableHosts()
            if (availableHosts.isNullOrEmpty()) {
                Timber.e("Error fetching available hosts")
                torrentLiveData.postEvent(TorrentEvent.DownloadedFileFailure)
            } else {
                val uploadedTorrent =
                    torrentsRepository.addTorrent(binaryTorrent, availableHosts.first().host)
                when (uploadedTorrent) {
                    is EitherResult.Failure -> {
                        networkExceptionLiveData.postEvent(uploadedTorrent.failure)
                        torrentLiveData.postEvent(TorrentEvent.DownloadedFileFailure)
                    }
                    is EitherResult.Success -> {
                        fetchTorrentDetails(uploadedTorrent.success.id)
                    }
                }
            }
        }
    }

    companion object {
        const val KEY_CURRENT_TORRENT = "current_torrent_key"
        const val KEY_CURRENT_TORRENT_ID = "current_torrent_id_key"
    }
}

sealed class TorrentEvent {
    data class Uploaded(val torrent: UploadedTorrent) : TorrentEvent()

    data class TorrentInfo(val item: TorrentItem) : TorrentEvent()

    data class FilesSelected(val torrent: TorrentItem) : TorrentEvent()

    data object SelectionUpdated : TorrentEvent()

    data object DownloadAll : TorrentEvent()

    data class DownloadSelection(val filesNumber: Int) : TorrentEvent()

    data object DownloadedFileSuccess : TorrentEvent()

    data object DownloadedFileFailure : TorrentEvent()

    data class DownloadedFileProgress(val progress: Int) : TorrentEvent()
}
