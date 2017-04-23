package io.github.initrc.bootstrap.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.widget.FrameLayout
import io.github.initrc.bootstrap.R
import io.github.initrc.bootstrap.presenter.HomePresenter
import kotlinx.android.synthetic.main.view_home.view.*
import util.inflate

/**
 * Home view.
 */
class HomeView : FrameLayout {
    val presenter: HomePresenter

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        inflate(R.layout.view_home, true)
        feedList.layoutManager = LinearLayoutManager(context)
        presenter = HomePresenter(feedList)
        presenter.loadFeed()
    }
}