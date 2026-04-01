package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.DownloadItem
import javax.inject.Inject
import retrofit2.Response

class DownloadApiHelperImpl @Inject constructor(private val downloadApi: DownloadApi) :
    DownloadApiHelper {

    override suspend fun getDownloads(
        token: String,
        offset: Int?,
        page: Int,
        limit: Int,
    ): Response<List<DownloadItem>> = downloadApi.getDownloads(token, offset, page, limit)

    override suspend fun deleteDownload(token: String, id: String) =
        downloadApi.deleteDownload(token, id)
}
