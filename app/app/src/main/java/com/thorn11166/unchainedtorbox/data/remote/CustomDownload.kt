package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.plugins.model.Plugin
import com.thorn11166.unchainedtorbox.repository.model.JsonPluginRepository
import com.thorn11166.unchainedtorbox.utilities.DEFAULT_PLUGINS_REPOSITORY_LINK
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface CustomDownload {

    @Streaming @GET suspend fun getFile(@Url url: String): Response<ResponseBody>

    @GET
    suspend fun getPluginsRepository(
        @Url repositoryUrl: String = DEFAULT_PLUGINS_REPOSITORY_LINK
    ): Response<JsonPluginRepository>

    @GET suspend fun getPlugin(@Url pluginUrl: String): Response<Plugin>

    @GET suspend fun getString(@Url url: String): Response<String>
}
