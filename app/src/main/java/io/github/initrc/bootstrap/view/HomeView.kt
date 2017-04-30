package io.github.initrc.bootstrap.view

import android.app.Activity
import android.content.Context
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.widget.FrameLayout
import io.github.initrc.bootstrap.R
import io.github.initrc.bootstrap.presenter.HomePresenter
import io.github.initrc.bootstrap.view.decoration.HorizontalEqualSpaceItemDecoration
import io.github.initrc.bootstrap.view.listener.InfiniteGridScrollListener
import kotlinx.android.synthetic.main.view_home.view.*
import util.DeviceUtils
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
        val gridColumnCount = resources.getInteger(R.integer.gridColumnCount)
        val padding = resources.getDimension(R.dimen.margin).toInt()
        feedList.layoutManager = StaggeredGridLayoutManager(
                GridUtils.getGridColumnCount(resources), StaggeredGridLayoutManager.VERTICAL)
        feedList.addItemDecoration(HorizontalEqualSpaceItemDecoration(padding))
        presenter = HomePresenter(feedList, getGridColumnWidthPx(gridColumnCount, padding))
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

    private fun getGridColumnWidthPx(gridColumnCount: Int, padding: Int): Int {
        val paddingCount = gridColumnCount + 1
        val screenWidth = DeviceUtils.getScreenWidthPx(context as Activity)
        return (screenWidth - padding * paddingCount) / gridColumnCount
    }
}