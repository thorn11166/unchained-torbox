package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.Stream
import retrofit2.Response

interface StreamingApiHelper {
    suspend fun getStreams(token: String, id: String): Response<Stream>
}
