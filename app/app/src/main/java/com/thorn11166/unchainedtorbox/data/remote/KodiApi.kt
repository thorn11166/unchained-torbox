package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.KodiGenericResponse
import com.thorn11166.unchainedtorbox.data.model.KodiRequest
import com.thorn11166.unchainedtorbox.data.model.KodiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface KodiApi {

    @POST("jsonrpc")
    suspend fun openUrl(
        @Body body: KodiRequest,
        @Header("Authorization") auth: String? = null,
        @Header("Content-Type") contentType: String = "application/json",
    ): Response<KodiResponse>

    @POST("jsonrpc")
    suspend fun getVolume(
        @Body body: KodiRequest,
        @Header("Authorization") auth: String? = null,
        @Header("Content-Type") contentType: String = "application/json",
    ): Response<KodiGenericResponse>
}
