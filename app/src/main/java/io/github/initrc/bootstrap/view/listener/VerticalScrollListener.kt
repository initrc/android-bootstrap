package io.github.initrc.bootstrap.view.listener

import android.support.v7.widget.RecyclerView

/**
 * Vertical scroll listener.
 */
class VerticalScrollListener(val onScrollUp: () -> Unit, val onScrollDown: () -> Unit)
        : RecyclerView.OnScrollListener() {
    var prevScrollY = 0 // 1 = up, -1 = down
    val scrollThreshold = 10
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > scrollThreshold && prevScrollY <= 0) {
            prevScrollY = 1
            onScrollUp()
        }
        if (dy < -scrollThreshold && prevScrollY >= 0) {
            prevScrollY = -1
            onScrollDown()
        }
    }
}
