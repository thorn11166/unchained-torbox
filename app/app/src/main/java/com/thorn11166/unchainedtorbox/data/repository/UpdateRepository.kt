package com.thorn11166.unchainedtorbox.data.repository

import com.thorn11166.unchainedtorbox.data.local.ProtoStore
import com.thorn11166.unchainedtorbox.data.model.Updates
import com.thorn11166.unchainedtorbox.data.remote.UpdateApiHelper
import com.thorn11166.unchainedtorbox.utilities.SIGNATURE
import javax.inject.Inject

class UpdateRepository
@Inject
constructor(protoStore: ProtoStore, private val updateApiHelper: UpdateApiHelper) :
    BaseRepository(protoStore) {

    suspend fun getUpdates(url: String = SIGNATURE.URL): Updates? {

        val response =
            safeApiCall(
                call = { updateApiHelper.getUpdates(url) },
                errorMessage = "Error getting updates",
            )

        return response
    }
}
