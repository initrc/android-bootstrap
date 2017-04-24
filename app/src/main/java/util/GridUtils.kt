package util

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Resources
import io.github.initrc.bootstrap.R

/**
 * Grid utils.
 */
object GridUtils {
    val gridColumnCountLandscapeRatio = 1.5f

    fun getGridColumnCount(resources: Resources) : Int {
        val col = resources.getInteger(R.integer.gridColumnCount)
        when (resources.configuration.orientation) {
            ORIENTATION_LANDSCAPE -> return Math.round(col * gridColumnCountLandscapeRatio)
            else -> return col
        }
    }
}
