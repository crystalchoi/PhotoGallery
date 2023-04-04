package com.bignerdranch.android.photogallery.api

import com.bignerdranch.android.photogallery.data.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "51121d8083644241cc0497af060ca2b4"

interface FlickrApi {
    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
            "&api_key=$API_KEY" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s"
    )
    suspend fun fetchPhotosOld(): FlickrResponse

    @GET("services/rest/?method=flickr.interestingness.getList") suspend fun fetchPhotos(): FlickrResponse

    @GET("services/rest?method=flickr.photos.search")
    suspend fun searchPhotos(@Query("text") query: String): FlickrResponse
}
