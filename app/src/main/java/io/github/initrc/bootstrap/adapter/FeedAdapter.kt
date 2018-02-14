package io.github.initrc.bootstrap.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.github.initrc.bootstrap.R
import io.github.initrc.bootstrap.model.Photo
import util.inflate
import util.loadUrl

/**
 * Feed adapter.
 */
class FeedAdapter : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    var items = mutableListOf<Photo>()
    var gridColumnWidth = 0

    fun addPhotos(photos: List<Photo>) {
        items.addAll(photos)
        notifyItemRangeChanged(itemCount - photos.size, photos.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = parent?.inflate(R.layout.view_photo_item)
        return ViewHolder(itemView!!)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val photo: Photo? = items.getOrNull(position)
        photo?.images?.getOrNull(0)?.let {
            measurePhoto(holder?.photoIv!!, photo.width, photo.height)
            holder.photoIv?.loadUrl(it.url)
        }
        holder?.nameTv?.text = photo?.name
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoIv = itemView.findViewById<ImageView>(R.id.photoIv)
        val nameTv = itemView.findViewById<TextView>(R.id.nameTv)
    }

    private fun measurePhoto(view: View, width: Int, height: Int) {
        if (width == 0 || height == 0) { return }
        view.layoutParams.width = gridColumnWidth
        view.layoutParams.height = height * gridColumnWidth / width
        view.requestLayout()
    }
}