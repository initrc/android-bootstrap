package io.github.initrc.bootstrap.view.listener

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * Infinite scroll listener.
 */
class InfiniteGridScrollListener(
        private val layoutManager: StaggeredGridLayoutManager, private val loadMore: () -> Unit)
        : RecyclerView.OnScrollListener() {
    private var prevTotalCount = 0
    private var totalCount = 0
    private var visibleCount = 0
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy < 0) return
        totalCount = layoutManager.itemCount
        visibleCount = layoutManager.childCount
        val visibleThreshold = layoutManager.spanCount * 5
        val firstVisiblePos = IntArray(layoutManager.spanCount)
        layoutManager.findFirstVisibleItemPositions(firstVisiblePos)

        if (loading) {
            if (totalCount != prevTotalCount) {
                prevTotalCount = totalCount
                loading = false
            }
        }
        if (!loading && totalCount - visibleCount
                <= firstVisiblePos.getOrElse(0) {0} + visibleThreshold) {
            loadMore()
            loading = true
        }
    }
}
