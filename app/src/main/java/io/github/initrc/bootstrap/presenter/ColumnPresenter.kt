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

    fun increaseCount() {
        if (columnCountType.ordinal < ColumnCountType.values().size - 1) {
            columnCountType = ColumnCountType.values()[columnCountType.ordinal + 1]
            onColumnUpdate(getDynamicGridColumnCount())
        }
    }

    fun decreaseCount() {
        if (columnCountType.ordinal > 0) {
            columnCountType = ColumnCountType.values()[columnCountType.ordinal - 1]
            onColumnUpdate(getDynamicGridColumnCount())
        }
    }

    fun setOnColumnUpdateCallback(func: (Int) -> Unit) {
        onColumnUpdate = func
    }
}

enum class ColumnCountType {
    ONE, STANDARD, DOUBLE
}
