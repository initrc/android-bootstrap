package io.github.initrc.bootstrap.presenter

import android.app.Activity
import io.github.initrc.bootstrap.R
import util.DeviceUtils
import util.GridUtils

/**
 * Presenter for the grid column.
 */
class ColumnPresenter(private val activity: Activity) {
    var columnCountType = ColumnCountType.STANDARD
    val resources = activity.resources!!
    val gridPadding = resources.getDimension(R.dimen.margin).toInt()
    private var onColumnUpdate: (Int) -> Unit = {}

    fun getDynamicGridColumnCount(): Int {
        return when(columnCountType) {
            ColumnCountType.ONE -> 1
            ColumnCountType.STANDARD -> GridUtils.getGridColumnCount(resources)
            ColumnCountType.DOUBLE -> GridUtils.getGridColumnCount(resources) * 2
        }
    }

    fun getGridColumnWidthPx(): Int {
        val gridColumnCount = getDynamicGridColumnCount()
        val paddingCount = gridColumnCount + 1
        val screenWidth = DeviceUtils.getScreenWidthPx(activity)
        return (screenWidth - gridPadding * paddingCount) / gridColumnCount
    }

    /**
     * @return True if the count is adjusted.
     */
    fun adjustCount(delta: Int): Boolean {
        if (delta == 0) return false
        if (columnCountType.ordinal + delta in 0 until ColumnCountType.values().size) {
            columnCountType = ColumnCountType.values()[columnCountType.ordinal + delta]
            onColumnUpdate(getDynamicGridColumnCount())
            return true
        }
        return false
    }

    fun setOnColumnUpdateCallback(func: (Int) -> Unit) {
        onColumnUpdate = func
    }
}

enum class ColumnCountType {
    ONE, STANDARD, DOUBLE
}
