package util

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Kotlin extensions.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false) : View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.snack(text: CharSequence?, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, text ?: "", duration).show()
}

fun ImageView.loadUrl(url: String) {
    Glide.with(this.context).load(url).crossFade().into(this)
}

fun AnimatorSet.onAnimationEnd(end: () -> Unit) {
    addListener(object: AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) { end() }
        override fun onAnimationCancel(animation: Animator?) { end() }
        override fun onAnimationStart(animation: Animator?) {}
    })
}
