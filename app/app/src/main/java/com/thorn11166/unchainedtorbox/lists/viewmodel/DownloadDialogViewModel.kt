package com.thorn11166.unchainedtorbox.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.thorn11166.unchainedtorbox.data.model.DownloadItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadDialogViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {

    fun setItem(item: DownloadItem?) {
        item.let { savedStateHandle[KEY_ITEM] = it }
    }

    fun getItem(): DownloadItem? {
        return savedStateHandle[KEY_ITEM]
    }

    companion object {
        private const val KEY_ITEM = "item_key"
    }
}
