package com.kinotech.kinotechappv1

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper

class NewGestureDetector(
    private val flipper: ViewFlipper,
    private val context: AuthActivity
) :
    GestureDetector.SimpleOnGestureListener() {
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val sensitivity = context.resources.getInteger(R.integer.sensitivity)
        if ((e1.x - e2.x) > sensitivity) {
            swipeLeft()
        } else if ((e2.x - e1.x) > sensitivity) {
            swipeRight()
        }
        return true
    }

    private fun isFirst(): Boolean {
        return flipper.displayedChild == 0
    }

    private fun isLast(): Boolean {
        return flipper.displayedChild + 1 == flipper.childCount
    }

    fun swipeLeft() {
        if (!isLast()) {
            flipper.inAnimation = AnimationUtils.loadAnimation(context, R.anim.flipin_reverse)
            flipper.outAnimation = AnimationUtils.loadAnimation(context, R.anim.flipout_reverse)
            flipper.showPrevious()
        }
    }

    fun swipeRight() {
        if (!isFirst()) {
            flipper.inAnimation = AnimationUtils.loadAnimation(context, R.anim.flipin)
            flipper.outAnimation = AnimationUtils.loadAnimation(context, R.anim.flipout)
            flipper.showNext()
        }
    }
}
