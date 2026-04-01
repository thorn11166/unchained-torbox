package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.Updates
import javax.inject.Inject
import retrofit2.Response

class UpdateApiHelperImpl @Inject constructor(private val updateApi: UpdateApi) : UpdateApiHelper {
    override suspend fun getUpdates(url: String): Response<Updates> = updateApi.getUpdates(url)
}
