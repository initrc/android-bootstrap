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
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener(columnPresenter, feedList))

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
        feedList.setOnTouchListener { _: View, event: MotionEvent ->
            scaleGestureDetector.onTouchEvent(event)
            event.pointerCount > 1 // don't scroll while performing multi-finger gestures
        }

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

    class ScaleListener(private val columnPresenter: ColumnPresenter, private val view: View)
            : SimpleOnScaleGestureListener() {
        private var previousSpan = 1f
        private var scaleFactor = 1f
        private val scaleThreshold = 0.3f
        private val ignoreScaleThreshold = 25
        private var ignoreScaleCount = 0 // workaround to ignore buggy scale events
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            if (detector == null) return false
            if (ignoreScaleCount > 0) {
                ignoreScaleCount--
                return false
            }
            var delta: Int
            scaleFactor *= getScaleFactor(previousSpan, detector.currentSpan)
            when {
                scaleFactor < 1 - scaleThreshold -> delta = 1
                scaleFactor > 1 + scaleThreshold -> delta = -1
                else -> {
                    delta = 0
                    scaleView(view, scaleFactor)
                }
            }
            if (delta != 0) {
                previousSpan = detector.currentSpan
                columnPresenter.adjustCount(delta)
                scaleFactor = 1f
                scaleView(view, scaleFactor)
                ignoreScaleCount = ignoreScaleThreshold
            }
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            scaleFactor = 1f
            previousSpan = detector?.currentSpan!!
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            scaleFactor = 1f
            scaleView(view, scaleFactor)
        }

        private fun getScaleFactor(previousSpan: Float, currentSpan: Float): Float {
            return if (previousSpan > 0 && currentSpan > 0) {
                currentSpan / previousSpan
            } else {
                1.0f
            }
        }

        private fun scaleView(view: View, scaleFactor: Float) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.alpha = 1 - Math.abs(scaleFactor - 1)
        }
    }
}