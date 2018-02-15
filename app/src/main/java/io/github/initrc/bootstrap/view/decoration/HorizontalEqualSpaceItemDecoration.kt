package io.github.initrc.bootstrap.view.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Equal space item decoration for grid view.
 */
class HorizontalEqualSpaceItemDecoration(space: Int) : RecyclerView.ItemDecoration() {
    private val halfSpace = space / 2

    override fun getItemOffsets(
            outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        if (parent?.paddingLeft != halfSpace) {
            parent?.setPadding(halfSpace, parent?.paddingTop, halfSpace, parent.paddingBottom)
            parent?.clipToPadding = false
        }

        outRect?.left = halfSpace
        outRect?.right = halfSpace
    }
}
