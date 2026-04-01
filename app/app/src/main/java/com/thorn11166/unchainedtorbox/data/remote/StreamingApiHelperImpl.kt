package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.Stream
import javax.inject.Inject
import retrofit2.Response

class StreamingApiHelperImpl @Inject constructor(private val streamingApi: StreamingApi) :
    StreamingApiHelper {
    override suspend fun getStreams(token: String, id: String): Response<Stream> =
        streamingApi.getStreams(token, id)
}
