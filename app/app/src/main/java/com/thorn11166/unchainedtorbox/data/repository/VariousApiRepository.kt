package com.thorn11166.unchainedtorbox.data.repository

import com.thorn11166.unchainedtorbox.data.local.ProtoStore
import com.thorn11166.unchainedtorbox.data.remote.VariousApiHelper
import javax.inject.Inject

class VariousApiRepository
@Inject
constructor(protoStore: ProtoStore, private val variousApiHelper: VariousApiHelper) :
    BaseRepository(protoStore) {

    suspend fun disableToken(): Unit? {

        val response =
            safeApiCall(
                call = { variousApiHelper.disableToken(token = "Bearer ${getToken()}") },
                errorMessage = "Error disabling token",
            )

        return response
    }
}
