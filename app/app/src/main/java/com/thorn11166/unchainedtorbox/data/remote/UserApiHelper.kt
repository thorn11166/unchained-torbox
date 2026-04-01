package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.User
import retrofit2.Response

interface UserApiHelper {
    suspend fun getUser(token: String): Response<User>
}
