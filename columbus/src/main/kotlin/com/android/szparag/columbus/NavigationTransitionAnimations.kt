@file:Suppress("unused")

package com.android.szparag.columbus

import android.animation.TimeInterpolator
import android.view.animation.AccelerateDecelerateInterpolator

typealias Millis = Long

sealed class NavigationTransitionAnimation {
  companion object Constants {
    val DURATION_SHORT: Millis = 250
    val DURATION_NORMAL: Millis = 1000
    val DURATION_LONG: Millis = 1000
    val DEGREES_DIRECTION_RIGHT = 180
    val DEGREES_DIRECTION_LEFT = 0
    val DEGREES_DIRECTION_TOP = 90
    val DEGREES_DIRECTION_BOTTOM = 270
  }

  class INSTANT : NavigationTransitionAnimation()

}

//todo: scale_in
sealed class NavigationTransitionInAnimation : NavigationTransitionAnimation() {
  class FADE_IN(
      val interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
      val duration: Millis = DURATION_NORMAL
  ) : NavigationTransitionInAnimation()

  class MOVE_IN(
      val degrees: Int = DEGREES_DIRECTION_RIGHT,
      val interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
      val duration: Millis = DURATION_NORMAL
  ) : NavigationTransitionInAnimation()

  override fun toString(): String = this::class.java.simpleName
}

//todo: scale_out
sealed class NavigationTransitionOutAnimation : NavigationTransitionAnimation() {
  class FADE_OUT(
      val interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
      val duration: Millis = DURATION_NORMAL
  ) : NavigationTransitionOutAnimation()

  class MOVE_OUT(
      val degrees: Int = DEGREES_DIRECTION_LEFT,
      val interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
      val duration: Millis = DURATION_NORMAL
  ) : NavigationTransitionOutAnimation()

  override fun toString(): String = this::class.java.simpleName

}