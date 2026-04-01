package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.plugins.model.Plugin
import com.thorn11166.unchainedtorbox.repository.model.JsonPluginRepository
import okhttp3.ResponseBody
import retrofit2.Response

interface CustomDownloadHelper {

    suspend fun getFile(url: String): Response<ResponseBody>

    suspend fun getPluginsRepository(repositoryUrl: String): Response<JsonPluginRepository>

    suspend fun getPlugin(pluginUrl: String): Response<Plugin>

    suspend fun getAsString(url: String): Response<String>
}
