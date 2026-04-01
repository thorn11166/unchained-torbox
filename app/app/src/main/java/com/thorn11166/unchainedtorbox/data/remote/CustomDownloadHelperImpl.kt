package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.plugins.model.Plugin
import com.thorn11166.unchainedtorbox.repository.model.JsonPluginRepository
import javax.inject.Inject
import okhttp3.ResponseBody
import retrofit2.Response

class CustomDownloadHelperImpl @Inject constructor(private val customDownload: CustomDownload) :
    CustomDownloadHelper {
    override suspend fun getFile(url: String): Response<ResponseBody> = customDownload.getFile(url)

    override suspend fun getPluginsRepository(
        repositoryUrl: String
    ): Response<JsonPluginRepository> = customDownload.getPluginsRepository(repositoryUrl)

    override suspend fun getPlugin(pluginUrl: String): Response<Plugin> =
        customDownload.getPlugin(pluginUrl)

    override suspend fun getAsString(url: String): Response<String> = customDownload.getString(url)
}
