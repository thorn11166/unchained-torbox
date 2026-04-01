package com.thorn11166.unchainedtorbox.data.remote

import com.thorn11166.unchainedtorbox.data.model.TorBoxResponse
import com.thorn11166.unchainedtorbox.data.model.TorBoxListResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * TorBox API Retrofit interface
 * 
 * TorBox uses a single API key for all requests (passed as query param or header).
 * This interface manages all REST calls to TorBox API endpoints.
 * 
 * Note: TorBox API structure may differ from Real-Debrid.
 * Endpoints and parameters should be adapted based on actual TorBox documentation.
 */
interface TorBoxApi {

    /**
     * Get user account information
     */
    @GET("api/user/profile")
    suspend fun getUserProfile(
        @Query("api_key") apiKey: String
    ): Response<TorBoxResponse<UserProfile>>

    /**
     * Get available hosts/sources for debrid
     */
    @GET("api/available_hosts")
    suspend fun getAvailableHosts(
        @Query("api_key") apiKey: String
    ): Response<TorBoxResponse<List<AvailableHostData>>>

    /**
     * Get torrent information by ID
     */
    @GET("api/torrent/{id}")
    suspend fun getTorrentInfo(
        @Query("api_key") apiKey: String,
        @Path("id") id: String
    ): Response<TorBoxResponse<TorrentData>>

    /**
     * Add a torrent file to the queue
     */
    @PUT("api/torrents/add")
    suspend fun addTorrent(
        @Query("api_key") apiKey: String,
        @Body binaryTorrent: RequestBody,
        @Query("host") host: String? = null
    ): Response<TorBoxResponse<AddTorrentResponse>>

    /**
     * Add a magnet link to the queue
     */
    @FormUrlEncoded
    @POST("api/torrents/addMagnet")
    suspend fun addMagnet(
        @Query("api_key") apiKey: String,
        @Field("magnet") magnet: String,
        @Field("host") host: String? = null
    ): Response<TorBoxResponse<AddTorrentResponse>>

    /**
     * Get list of user's torrents
     * 
     * @param apiKey TorBox API key
     * @param offset Starting offset
     * @param limit Items per page (0-100)
     * @param filter Filter active/completed torrents
     */
    @GET("api/torrents")
    suspend fun getTorrentsList(
        @Query("api_key") apiKey: String,
        @Query("offset") offset: Int? = 0,
        @Query("limit") limit: Int? = 50,
        @Query("filter") filter: String? = null
    ): Response<TorBoxListResponse<TorrentData>>

    /**
     * Select files from a torrent to download
     * 
     * @param apiKey TorBox API key
     * @param id Torrent ID
     * @param files File IDs (comma-separated) or "all"
     */
    @FormUrlEncoded
    @POST("api/torrents/{id}/selectFiles")
    suspend fun selectFiles(
        @Query("api_key") apiKey: String,
        @Path("id") id: String,
        @Field("files") files: String = "all"
    ): Response<TorBoxResponse<Unit>>

    /**
     * Delete a torrent from queue
     */
    @DELETE("api/torrents/{id}")
    suspend fun deleteTorrent(
        @Query("api_key") apiKey: String,
        @Path("id") id: String
    ): Response<TorBoxResponse<Unit>>

    /**
     * Get unrestricted link for a file
     */
    @FormUrlEncoded
    @POST("api/unrestrict/link")
    suspend fun unrestrictLink(
        @Query("api_key") apiKey: String,
        @Field("link") link: String
    ): Response<TorBoxResponse<UnrestrictedLink>>

    /**
     * Check if a link is supported by TorBox
     */
    @FormUrlEncoded
    @POST("api/unrestrict/check")
    suspend fun checkLink(
        @Query("api_key") apiKey: String,
        @Field("link") link: String
    ): Response<TorBoxResponse<LinkCheckResult>>

    /**
     * Get API usage/quota information
     */
    @GET("api/user/limits")
    suspend fun getUserLimits(
        @Query("api_key") apiKey: String
    ): Response<TorBoxResponse<UserLimits>>
}

// ===== Data Models for TorBox API =====

/**
 * User profile information
 */
data class UserProfile(
    val id: String,
    val username: String,
    val email: String?,
    val premium: Boolean = false,
    val expirationDate: Long? = null,
    val remainingTraffic: Long? = null,
    val totalTraffic: Long? = null
)

/**
 * Available host/source information
 */
data class AvailableHostData(
    val id: String,
    val name: String,
    val image: String? = null,
    val supported: Boolean = true
)

/**
 * Torrent data from TorBox API
 */
data class TorrentData(
    val id: String,
    val hash: String? = null,
    val name: String,
    val size: Long? = null,
    val progress: Int? = null,
    val status: String? = null, // downloading, completed, error, etc.
    val addedDate: Long? = null,
    val completedDate: Long? = null,
    val downloadSpeed: Long? = null,
    val seedingSpeed: Long? = null,
    val seeders: Int? = null,
    val leechers: Int? = null,
    val files: List<TorrentFile> = emptyList()
)

/**
 * File within a torrent
 */
data class TorrentFile(
    val id: String,
    val name: String,
    val size: Long,
    val priority: Int? = null
)

/**
 * Response from adding a torrent
 */
data class AddTorrentResponse(
    val id: String,
    val name: String? = null,
    val addedDate: Long? = null
)

/**
 * Unrestricted link result
 */
data class UnrestrictedLink(
    val link: String,
    val filename: String? = null,
    val filesize: Long? = null,
    val host: String? = null
)

/**
 * Link check result
 */
data class LinkCheckResult(
    val link: String,
    val supported: Boolean,
    val host: String? = null,
    val filename: String? = null,
    val filesize: Long? = null
)

/**
 * User quota/limits
 */
data class UserLimits(
    val remainingTraffic: Long? = null,
    val totalTraffic: Long? = null,
    val remainingPoints: Long? = null,
    val totalPoints: Long? = null,
    val rateLimitRemaining: Int? = null
)
