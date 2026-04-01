package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.KodiGenericResponse
import com.thorn11166.unchainedtorbox.data.model.KodiRequest
import com.thorn11166.unchainedtorbox.data.model.KodiResponse
import retrofit2.Response

interface KodiApiHelper {
    suspend fun openUrl(request: KodiRequest, auth: String?): Response<KodiResponse>

    suspend fun getVolume(request: KodiRequest, auth: String?): Response<KodiGenericResponse>
}
