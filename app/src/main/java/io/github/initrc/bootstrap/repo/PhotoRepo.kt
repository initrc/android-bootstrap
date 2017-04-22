package io.github.initrc.bootstrap.repo

import io.github.initrc.bootstrap.api.PhotoApi
import io.github.initrc.bootstrap.model.Photo
import io.reactivex.Observable

/**
 * Photo repo.
 */
object PhotoRepo {
    fun getPhotos(feature: String) : Observable<List<Photo>> {
        return Observable.create {
            subscriber ->
            val response = PhotoApi.getPhotos(feature).execute()
            if (response.isSuccessful) {
                val photos = response.body().photos.map { Photo(it.name, it.images) }
                subscriber.onNext(photos)
                subscriber.onComplete()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}
