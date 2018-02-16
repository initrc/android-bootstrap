package io.github.initrc.bootstrap.view

import android.app.Activity
import android.content.Context
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import io.github.initrc.bootstrap.R
import io.github.initrc.bootstrap.presenter.ColumnCountType
import io.github.initrc.bootstrap.presenter.ColumnPresenter
import io.github.initrc.bootstrap.presenter.HomePresenter
import io.github.initrc.bootstrap.view.decoration.HorizontalEqualSpaceItemDecoration
import io.github.initrc.bootstrap.view.listener.InfiniteGridScrollListener
import io.github.initrc.bootstrap.view.listener.VerticalScrollListener
import kotlinx.android.synthetic.main.view_home.view.*
import util.AnimationUtils
import util.inflate

/**
 * Home view.
 */
class HomeView : FrameLayout {
    private val presenter: HomePresenter
    private val columnPresenter: ColumnPresenter
    private val scaleGestureDetector: ScaleGestureDetector

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        inflate(R.layout.view_home, true)
        // dynamic columns
        columnPresenter = ColumnPresenter(context as Activity)
        val gridColumnCount = columnPresenter.getDynamicGridColumnCount()
        presenter = HomePresenter(feedList, columnPresenter.getGridColumnWidthPx(), homeFab)
        columnPresenter.setOnColumnUpdateCallback { columnCount ->
            presenter.setGridColumnWidth(columnPresenter.getGridColumnWidthPx())
            presenter.setImageOnly(columnPresenter.columnCountType.ordinal > ColumnCountType.STANDARD.ordinal)
            (feedList.layoutManager as StaggeredGridLayoutManager).spanCount = columnCount
            feedList.adapter.notifyItemRangeChanged(0, feedList.adapter.itemCount)
        }
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener(columnPresenter))

        // feed
        feedList.layoutManager = StaggeredGridLayoutManager(
                gridColumnCount, StaggeredGridLayoutManager.VERTICAL)
        feedList.addItemDecoration(HorizontalEqualSpaceItemDecoration(columnPresenter.gridPadding))
        feedList.clearOnScrollListeners()
        feedList.addOnScrollListener(
                InfiniteGridScrollListener(feedList.layoutManager as StaggeredGridLayoutManager)
                { presenter.loadPhotos() })
        feedList.addOnScrollListener(VerticalScrollListener(
                { AnimationUtils.hideFab(homeFab) }, { AnimationUtils.showFab(homeFab) }))
        feedList.setOnTouchListener({ _: View, event: MotionEvent ->
            scaleGestureDetector.onTouchEvent(event)
            event.pointerCount > 1 // don't scroll while performing multi-finger gestures
        })

        // slide select view
        val slideSelectViewBuilder = SlideSelectView.Builder(context)
                .setSelectedIndex(presenter.features.indexOf(presenter.currentFeature))
                .setOnDismiss {
                    AnimationUtils.showFab(homeFab)
                }
        for (feature in presenter.features) {
            slideSelectViewBuilder.addItem(getFeatureName(feature),
                    View.OnClickListener{ presenter.refreshPhotos(feature) })
        }
        val dragSelectView = slideSelectViewBuilder.build()
        dragSelectView.visibility = View.GONE
        homeRootView.addView(dragSelectView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        // fab
        homeFab.setOnClickListener {
            AnimationUtils.hideFab(homeFab)
            dragSelectView.show()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onBind()
    }

    override fun onDetachedFromWindow() {
        presenter.onUnbind()
        super.onDetachedFromWindow()
    }

    private fun getFeatureName(feature: String): String {
        when (feature) {
            presenter.features[0] -> return context.getString(R.string.popular)
            presenter.features[1] -> return context.getString(R.string.editors)
            presenter.features[2] -> return context.getString(R.string.upcoming)
            presenter.features[3] -> return context.getString(R.string.fresh)
        }
        return ""
    }

    class ScaleListener(private val columnPresenter: ColumnPresenter) : SimpleOnScaleGestureListener() {
        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            super.onScaleEnd(detector)
            if (detector == null) return
            if (detector.scaleFactor < 1) {
                columnPresenter.increaseCount()
            } else if (detector.scaleFactor > 1) {
                columnPresenter.decreaseCount()
            }
        }
   }
}