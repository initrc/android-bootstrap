package io.github.initrc.bootstrap.api

import io.github.initrc.bootstrap.model.PhotoResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Pixabay photo API.
 */
object PhotoApi {
    private val photoService: PhotoService
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        photoService = retrofit.create(PhotoService::class.java)
    }

    fun getPhotos(query: String, page: Int): Call<PhotoResponse> {
        return photoService.getPhotos(query, page)
    }
}
