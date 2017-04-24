package io.github.initrc.bootstrap.view

import android.content.Context
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.widget.FrameLayout
import io.github.initrc.bootstrap.R
import io.github.initrc.bootstrap.presenter.HomePresenter
import io.github.initrc.bootstrap.view.decoration.HorizontalEqualSpaceItemDecoration
import io.github.initrc.bootstrap.view.listener.InfiniteGridScrollListener
import kotlinx.android.synthetic.main.view_home.view.*
import util.GridUtils
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
        feedList.layoutManager = StaggeredGridLayoutManager(
                GridUtils.getGridColumnCount(resources), StaggeredGridLayoutManager.VERTICAL)
        feedList.addItemDecoration(HorizontalEqualSpaceItemDecoration(
                resources.getDimension(R.dimen.margin).toInt()))
        presenter = HomePresenter(feedList)
        feedList.clearOnScrollListeners()
        feedList.addOnScrollListener(
                InfiniteGridScrollListener(feedList.layoutManager as StaggeredGridLayoutManager)
                { presenter.loadPhotos() })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onBind()
    }

    override fun onDetachedFromWindow() {
        presenter.onUnbind()
        super.onDetachedFromWindow()
    }
}