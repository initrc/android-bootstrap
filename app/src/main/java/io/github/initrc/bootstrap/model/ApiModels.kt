package io.github.initrc.bootstrap.model

/**
 * Photos API model.
 */
class PhotoResponse(val hits : List<Photo>)
class Photo(val tags: String, val imageWidth: Int, val imageHeight: Int, val largeImageURL: String, val webformatURL: String)
