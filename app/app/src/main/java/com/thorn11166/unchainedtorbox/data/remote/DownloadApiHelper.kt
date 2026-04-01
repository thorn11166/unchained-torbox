package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.DownloadItem
import retrofit2.Response

interface DownloadApiHelper {
    suspend fun getDownloads(
        token: String,
        offset: Int?,
        page: Int,
        limit: Int,
    ): Response<List<DownloadItem>>

    suspend fun deleteDownload(token: String, id: String): Response<Unit>
}
