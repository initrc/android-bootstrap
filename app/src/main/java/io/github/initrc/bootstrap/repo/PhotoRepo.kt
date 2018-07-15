package io.github.initrc.bootstrap.repo

import io.github.initrc.bootstrap.api.PhotoApi
import io.github.initrc.bootstrap.model.Photo
import io.github.initrc.bootstrap.model.PhotoResponse
import io.reactivex.Observable

/**
 * Photo repo.
 */
object PhotoRepo {
    fun getPhotos(query: String, page: Int) : Observable<PhotoResponse> {
        return Observable.create {
            subscriber ->
            val response = PhotoApi.getPhotos(query, page).execute()
            if (response.isSuccessful) {
                response.body().let {
                    val photos = it.hits.map { Photo(it.tags, it.imageWidth, it.imageHeight, it.largeImageURL, it.webformatURL) }
                    val photoResponse = PhotoResponse(photos)
                    subscriber.onNext(photoResponse)
                    subscriber.onComplete()
                }
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}
