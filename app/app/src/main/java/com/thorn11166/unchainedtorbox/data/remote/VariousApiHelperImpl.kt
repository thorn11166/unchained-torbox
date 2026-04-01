package com.thorn11166.unchainedtorbox.data.remote

import javax.inject.Inject
import retrofit2.Response

class VariousApiHelperImpl @Inject constructor(private val variousApi: VariousApi) :
    VariousApiHelper {
    override suspend fun disableToken(token: String): Response<Unit> =
        variousApi.disableToken(token)
}
