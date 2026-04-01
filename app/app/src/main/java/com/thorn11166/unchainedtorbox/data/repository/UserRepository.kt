package com.thorn11166.unchainedtorbox.data.repository

import com.thorn11166.unchainedtorbox.data.local.ProtoStore
import com.thorn11166.unchainedtorbox.data.model.UnchainedNetworkException
import com.thorn11166.unchainedtorbox.data.model.User
import com.thorn11166.unchainedtorbox.data.remote.UserApiHelper
import com.thorn11166.unchainedtorbox.utilities.EitherResult
import javax.inject.Inject

class UserRepository
@Inject
constructor(protoStore: ProtoStore, private val userApiHelper: UserApiHelper) :
    BaseRepository(protoStore) {

    suspend fun getUserInfo(token: String): User? {

        val userResponse =
            safeApiCall(
                call = { userApiHelper.getUser("Bearer $token") },
                errorMessage = "Error Fetching User Info",
            )

        return userResponse
    }

    suspend fun getUserOrError(token: String): EitherResult<UnchainedNetworkException, User> {

        val userResponse =
            eitherApiResult(
                call = { userApiHelper.getUser("Bearer $token") },
                errorMessage = "Error Fetching User Info",
            )

        return userResponse
    }
}
