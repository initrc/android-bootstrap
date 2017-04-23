package io.github.initrc.bootstrap.repo

import io.github.initrc.bootstrap.model.Photo
import io.reactivex.Observable

/**
 * Photo repo.
 */
object PhotoRepo {
    fun getPhotos() : Observable<List<Photo>> {
        return Observable.create {
            subscriber ->
            val photos = listOf("a", "b", "c").map(::Photo)
            subscriber.onNext(photos)
        }
    }
}
