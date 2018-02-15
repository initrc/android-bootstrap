package util

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Resources
import io.github.initrc.bootstrap.R

/**
 * Grid utils.
 */
object GridUtils {
    private const val gridColumnCountLandscapeRatio = 1.5f

    fun getGridColumnCount(resources: Resources) : Int {
        val col = resources.getInteger(R.integer.gridColumnCount)
        return when (resources.configuration.orientation) {
            ORIENTATION_LANDSCAPE -> Math.round(col * gridColumnCountLandscapeRatio)
            else -> col
        }
    }
}
