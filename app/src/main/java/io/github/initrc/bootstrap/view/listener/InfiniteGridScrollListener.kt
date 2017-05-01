package io.github.initrc.bootstrap.view.listener

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * Infinite scroll listener.
 */
class InfiniteGridScrollListener(
        val layoutManager: StaggeredGridLayoutManager, val func: () -> Unit)
        : RecyclerView.OnScrollListener() {
    var prevTotalCount = 0
    var totalCount = 0
    var visibleCount = 0
    val visibleThreshold = layoutManager.spanCount * 5
    var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy <= 0) return
        totalCount = layoutManager.itemCount
        visibleCount = layoutManager.childCount
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
            func()
            loading = true
        }
    }
}
