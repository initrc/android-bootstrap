package io.github.initrc.bootstrap.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import io.github.initrc.bootstrap.model.Photo
import util.inflate

/**
 * Feed adapter.
 */
class FeedAdapter : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    var data = mutableListOf<Photo>()

    fun addPhotos(photos: List<Photo>) {
        data.addAll(photos)
        notifyItemRangeChanged(itemCount - photos.size, photos.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val tv = parent?.inflate(android.R.layout.simple_list_item_1) as TextView
        return ViewHolder(tv)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.tv?.text = data.getOrNull(position)?.name
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(_tv: TextView) : RecyclerView.ViewHolder(_tv) {
        val tv = _tv
    }
}