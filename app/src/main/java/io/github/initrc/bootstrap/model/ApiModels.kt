package io.github.initrc.bootstrap.model

/**
 * Photos API model.
 * https://api.500px.com/v1/photos?feature=editors&sort=created_at&image_size=4&page=1
 */
class PhotoResponse(val currentPage: Int, val totalPages: Int, val photos: List<Photo>)
class Photo(val name: String, val images: List<Image>)
class Image(val url: String)