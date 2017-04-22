package util

import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Kotlin extensions.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false) : View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.snack(text: CharSequence?, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, text ?: "", duration).show()
}
