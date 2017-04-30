package io.github.initrc.bootstrap.repo

import io.github.initrc.bootstrap.api.PhotoApi
import io.github.initrc.bootstrap.model.Photo
import io.github.initrc.bootstrap.model.PhotoResponse
import io.reactivex.Observable

/**
 * Photo repo.
 */
object PhotoRepo {
    fun getPhotos(feature: String, page: Int) : Observable<PhotoResponse> {
        return Observable.create {
            subscriber ->
            val response = PhotoApi.getPhotos(feature, page).execute()
            if (response.isSuccessful) {
                response.body().let {
                    val photos = it.photos.map { Photo(it.name, it.width, it.height, it.images) }
                    val photoResponse = PhotoResponse(it.current_page,  photos)
                    subscriber.onNext(photoResponse)
                    subscriber.onComplete()
                }
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}
