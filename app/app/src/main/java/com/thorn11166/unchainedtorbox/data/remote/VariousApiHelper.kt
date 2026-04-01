package com.thorn11166.unchainedtorbox.data.remote

import retrofit2.Response

interface VariousApiHelper {
    suspend fun disableToken(token: String): Response<Unit>
}
