package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.DownloadItem
import okhttp3.RequestBody
import retrofit2.Response

interface UnrestrictApiHelper {

    suspend fun getUnrestrictedLink(
        token: String,
        link: String,
        password: String? = null,
        remote: Int? = null,
    ): Response<DownloadItem>

    suspend fun getUnrestrictedFolder(token: String, link: String): Response<List<String>>

    suspend fun uploadContainer(token: String, container: RequestBody): Response<List<String>>

    suspend fun getContainerLinks(token: String, link: String): Response<List<String>>
}
