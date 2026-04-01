package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.Host
import retrofit2.Response

interface HostsApiHelper {
    suspend fun getHostsStatus(token: String): Response<Host>

    suspend fun getHostsRegex(): Response<List<String>>

    suspend fun getHostsFoldersRegex(): Response<List<String>>
}
