package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.Authentication
import com.thorn11166.unchainedtorbox.data.model.Secrets
import com.thorn11166.unchainedtorbox.data.model.Token
import javax.inject.Inject
import retrofit2.Response

class AuthApiHelperImpl @Inject constructor(private val authenticationApi: AuthenticationApi) :
    AuthApiHelper {

    override suspend fun getAuthentication(): Response<Authentication> =
        authenticationApi.getAuthentication()

    override suspend fun getSecrets(deviceCode: String): Response<Secrets> =
        authenticationApi.getSecrets(deviceCode = deviceCode)

    override suspend fun getToken(
        clientId: String,
        clientSecret: String,
        code: String,
    ): Response<Token> = authenticationApi.getToken(clientId, clientSecret, code)
}
