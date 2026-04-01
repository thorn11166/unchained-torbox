package com.thorn11166.unchainedtorbox.data.repository

import com.thorn11166.unchainedtorbox.data.local.ProtoStore
import com.thorn11166.unchainedtorbox.data.model.DownloadItem
import com.thorn11166.unchainedtorbox.data.remote.DownloadApiHelper
import javax.inject.Inject

class DownloadRepository
@Inject
constructor(protoStore: ProtoStore, private val downloadApiHelper: DownloadApiHelper) :
    BaseRepository(protoStore) {
    suspend fun getDownloads(offset: Int?, page: Int = 1, limit: Int = 50): List<DownloadItem> {

        val downloadResponse =
            safeApiCall(
                call = {
                    downloadApiHelper.getDownloads("Bearer ${getToken()}", offset, page, limit)
                },
                errorMessage = "Error Fetching Downloads list or list empty",
            )

        return downloadResponse ?: emptyList()
    }

    suspend fun deleteDownload(id: String): Unit? {

        val response =
            safeApiCall(
                call = {
                    downloadApiHelper.deleteDownload(token = "Bearer ${getToken()}", id = id)
                },
                errorMessage = "Error deleting download",
            )

        return response
    }
}
