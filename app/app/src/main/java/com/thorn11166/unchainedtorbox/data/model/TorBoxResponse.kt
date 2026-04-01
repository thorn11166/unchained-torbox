package com.thorn11166.unchainedtorbox.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * TorBox API response wrapper.
 * Most TorBox endpoints wrap data in this structure.
 *
 * @param data The actual response data (generic)
 * @param error Error message if request failed
 * @param success Boolean indicating if the request succeeded
 */
@JsonClass(generateAdapter = true)
data class TorBoxResponse<T>(
    @Json(name = "data")
    val data: T? = null,
    
    @Json(name = "error")
    val error: String? = null,
    
    @Json(name = "success")
    val success: Boolean = false
) {
    fun isSuccess(): Boolean = success && data != null
    fun getErrorMessage(): String? = if (!success) error else null
}

/**
 * Alternative wrapper for list responses from TorBox
 */
@JsonClass(generateAdapter = true)
data class TorBoxListResponse<T>(
    @Json(name = "data")
    val data: List<T> = emptyList(),
    
    @Json(name = "error")
    val error: String? = null,
    
    @Json(name = "success")
    val success: Boolean = false,
    
    @Json(name = "total")
    val total: Int? = null
) {
    fun isSuccess(): Boolean = success
    fun getErrorMessage(): String? = if (!success) error else null
}

/**
 * Pagination metadata for list responses
 */
@JsonClass(generateAdapter = true)
data class TorBoxPagination(
    @Json(name = "offset")
    val offset: Int? = null,
    
    @Json(name = "limit")
    val limit: Int? = null,
    
    @Json(name = "total")
    val total: Int? = null
)
