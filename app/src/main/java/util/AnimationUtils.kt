package util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Animation utils.
 */
object AnimationUtils {
    fun show(view: View, alpha: Boolean = true, scale: Boolean = false, rotate: Boolean = false) {
        if (view.visibility == View.VISIBLE) return
        view.visibility = View.VISIBLE
        val animators = ArrayList<Animator>().apply {
            if (alpha) add(ObjectAnimator.ofFloat(view, "alpha", 0f, 1f))
            if (scale) add(ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f))
            if (scale) add(ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f))
            if (rotate) add(ObjectAnimator.ofFloat(view, "rotation", 270f, 0f))
        }
        AnimatorSet().apply {
            playTogether(animators)
            start()
        }
    }

    fun hide(view: View, alpha: Boolean = true, scale: Boolean = false, rotate: Boolean = false) {
        if (view.visibility != View.VISIBLE) return
        val animators = ArrayList<Animator>().apply {
            if (alpha) add(ObjectAnimator.ofFloat(view, "alpha", 0f))
            if (scale) add(ObjectAnimator.ofFloat(view, "scaleX", 0f))
            if (scale) add(ObjectAnimator.ofFloat(view, "scaleY", 0f))
            if (rotate) add(ObjectAnimator.ofFloat(view, "rotation", 270f))
        }
        AnimatorSet().apply {
            playTogether(animators)
            onAnimationEnd({ view.visibility = View.GONE })
            start()
        }
    }

    fun showFab(view: View) {
        show(view, true, true, true)
    }

    fun hideFab(view: View) {
        hide(view, true, true, true)
    }
}
