package util

import android.app.Activity
import android.util.DisplayMetrics



/**
 * Device utils.
 */
object DeviceUtils {
    fun getScreenWidthPx(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics)
        return metrics.widthPixels
    }
}
