package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.User
import javax.inject.Inject
import retrofit2.Response

class UserApiHelperImpl @Inject constructor(private val userApi: UserApi) : UserApiHelper {
    override suspend fun getUser(token: String): Response<User> = userApi.getUser(token)
}
