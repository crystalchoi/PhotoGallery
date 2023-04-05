package com.bignerdranch.android.photogallery.data

import android.net.Uri
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GalleryItem(
    val title: String,
    val id: String,
    val owner: String,
    @Json(name = "url_s") val url: String,
) {
    val photoPageUri: Uri
        get() = Uri.parse("https://www.flickr.com/photos/")
        .buildUpon()
        .appendPath(owner)
        .appendPath(id)
        .build()  // https://www.flickr.com/photos/owner/id
}
