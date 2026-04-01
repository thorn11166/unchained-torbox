package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.Authentication
import com.thorn11166.unchainedtorbox.data.model.Secrets
import com.thorn11166.unchainedtorbox.data.model.Token
import retrofit2.Response

interface AuthApiHelper {

    suspend fun getAuthentication(): Response<Authentication>

    suspend fun getSecrets(deviceCode: String): Response<Secrets>

    suspend fun getToken(clientId: String, clientSecret: String, code: String): Response<Token>
}
