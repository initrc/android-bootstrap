package io.github.initrc.bootstrap.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.github.initrc.bootstrap.R
import io.github.initrc.bootstrap.presenter.HomePresenter

/**
 * Home view.
 */
class HomeView : FrameLayout {
    val presenter: HomePresenter
    val layoutManager: RecyclerView.LayoutManager
    val feedView: RecyclerView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        View.inflate(context, R.layout.view_home, this)
        layoutManager = LinearLayoutManager(context)
        feedView = findViewById(R.id.feed_view) as RecyclerView
        feedView.layoutManager = layoutManager

        presenter = HomePresenter(feedView)
        presenter.loadFeed()
    }
}