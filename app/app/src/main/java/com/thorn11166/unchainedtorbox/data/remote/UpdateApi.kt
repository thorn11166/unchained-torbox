package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.Updates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface UpdateApi {
    @GET suspend fun getUpdates(@Url url: String): Response<Updates>
}

interface UpdateApiHelper {

    suspend fun getUpdates(url: String): Response<Updates>
}
