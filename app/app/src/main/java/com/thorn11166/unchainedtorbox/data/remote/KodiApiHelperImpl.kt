package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.KodiGenericResponse
import com.thorn11166.unchainedtorbox.data.model.KodiRequest
import com.thorn11166.unchainedtorbox.data.model.KodiResponse
import retrofit2.Response

class KodiApiHelperImpl(private val kodiApi: KodiApi) : KodiApiHelper {
    override suspend fun openUrl(request: KodiRequest, auth: String?): Response<KodiResponse> =
        kodiApi.openUrl(request, auth)

    override suspend fun getVolume(
        request: KodiRequest,
        auth: String?,
    ): Response<KodiGenericResponse> = kodiApi.getVolume(request, auth)
}
