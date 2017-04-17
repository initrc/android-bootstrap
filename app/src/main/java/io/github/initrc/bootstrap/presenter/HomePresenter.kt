package io.github.initrc.bootstrap.presenter

import android.support.v7.widget.RecyclerView
import io.github.initrc.bootstrap.adapter.FeedAdapter

/**
 * Presenter for home view.
 */
class HomePresenter (feedView: RecyclerView) {
    val feedAdapter: FeedAdapter = FeedAdapter()

    init {
        feedView.adapter = feedAdapter
    }

    fun loadFeed() {
        feedAdapter.data = mutableListOf("a", "b", "c")
    }
}
