package io.github.initrc.bootstrap.api

import io.github.initrc.bootstrap.model.PhotoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Pixabay photo service.
 */
interface PhotoService {
    @GET("api")
    fun getPhotos(@Query("q") query: String,
                  @Query("page") page: Int,
                  @Query("editors_choice") editorsChoice: Boolean  = ApiConstants.editorsChoice,
                  @Query("key") key: String = ApiSecrets.key)
            : Call<PhotoResponse>
}
