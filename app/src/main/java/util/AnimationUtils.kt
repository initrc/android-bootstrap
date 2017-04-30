package util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Animation utils.
 */
object AnimationUtils {
    fun showFab(view: View) {
        if (view.visibility == View.VISIBLE) return
        view.visibility = View.VISIBLE
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "rotation", 270f, 0f)
            )
            start()
        }
    }

    fun hideFab(view: View) {
        if (view.visibility != View.VISIBLE) return
        AnimatorSet().apply {
            playTogether(
                    ObjectAnimator.ofFloat(view, "alpha", 0f),
                    ObjectAnimator.ofFloat(view, "scaleX", 0f),
                    ObjectAnimator.ofFloat(view, "scaleY", 0f),
                    ObjectAnimator.ofFloat(view, "rotation", 270f)
            )
            onAnimationEnd({ view.visibility = View.GONE })
            start()
        }
    }
}
