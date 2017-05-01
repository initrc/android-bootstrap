package io.github.initrc.bootstrap.view

import android.app.Activity
import android.content.Context
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import io.github.initrc.bootstrap.R
import io.github.initrc.bootstrap.presenter.HomePresenter
import io.github.initrc.bootstrap.view.decoration.HorizontalEqualSpaceItemDecoration
import io.github.initrc.bootstrap.view.listener.InfiniteGridScrollListener
import io.github.initrc.bootstrap.view.listener.VerticalScrollListener
import kotlinx.android.synthetic.main.view_home.view.*
import util.AnimationUtils
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
        presenter = HomePresenter(feedList, getGridColumnWidthPx(gridColumnCount, padding), homeFab)
        feedList.clearOnScrollListeners()
        feedList.addOnScrollListener(
                InfiniteGridScrollListener(feedList.layoutManager as StaggeredGridLayoutManager)
                { presenter.loadPhotos() })
        feedList.addOnScrollListener(VerticalScrollListener(
                { AnimationUtils.hideFab(homeFab) }, { AnimationUtils.showFab(homeFab) }))

        // drag select view
        val dragSelectViewBuilder = DragSelectView.Builder(context)
                .setSelectedIndex(presenter.features.indexOf(presenter.currentFeature))
                .setOnDismiss {
                    AnimationUtils.showFab(homeFab)
                }
        for (feature in presenter.features) {
            dragSelectViewBuilder.addItem(getFeatureName(feature),
                    View.OnClickListener{ presenter.refreshPhotos(feature) })
        }
        val dragSelectView = dragSelectViewBuilder.build()
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

    private fun getGridColumnWidthPx(gridColumnCount: Int, padding: Int): Int {
        val paddingCount = gridColumnCount + 1
        val screenWidth = DeviceUtils.getScreenWidthPx(context as Activity)
        return (screenWidth - padding * paddingCount) / gridColumnCount
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
}