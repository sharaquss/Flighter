//@file:Suppress("unused")

package com.android.szparag.myextensionsandroid

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.LayoutAnimationController.AnimationParameters
import android.widget.ImageView
import com.android.szparag.myextensionsbase.findFirstIndexedOrNull
import kotlin.math.absoluteValue

typealias Widget = View

private val animatorListenerCallbackStub: (Animator?) -> Unit = {}
private val animationListenerCallbackStub: (Animation?) -> Unit = {}
private const val DEBUG_VIEW_STRING_DEFAULT_CAPACITY = 256

fun noIdString() = "NO-ID"

infix fun View.doOnClick(action: (View) -> Unit) =
    setOnClickListener(action)

fun Dp.toPx(context: Context) =
    (this * context.resources.displayMetrics.density).toInt()

fun ViewGroup.getChildren() =
    (0 until childCount).map { childIndex -> getChildAt(childIndex) }

fun ViewGroup.indexOfChildByClass(viewClass: Class<*>) = //todo: Class<*> -> Class<View>
    getChildren().findFirstIndexedOrNull { it::class.java == viewClass }.second

fun ViewGroup.childByClass(viewClass: Class<*>) = //todo: Class<*> -> Class<View>
    getChildren().findFirstIndexedOrNull { it::class.java == viewClass }.first

//<editor-fold desc="Widgets visibility">
fun Widget.hide() {
  if (visibility != GONE) this.visibility = GONE
}

fun Widget.show() {
  if (visibility != VISIBLE) visibility = VISIBLE
}
//</editor-fold>

//<editor-fold desc="Widgets resizing">
fun Widget.shrinkViewToZero() = configureLayoutParams(width = 0, height = 0)

fun Widget.configureLayoutParams(width: Int, height: Int, animationParams: AnimationParameters? = null) = this.layoutParams.apply {
  this.width = width
  this.height = height
  animationParams?.let { this.layoutAnimationParameters = it }
  this@configureLayoutParams.layoutParams = this
}


fun View.changeSize(squareSize: Int) = changeSize(squareSize, squareSize)

fun View.changeSize(width: Int, height: Int) {
  val changedLayoutParams = layoutParams
  changedLayoutParams.width = width
  changedLayoutParams.height = height
  layoutParams = changedLayoutParams
}

//</editor-fold>

//<editor-fold desc="Widgets position">
val Widget.locationInWindow
  get() = IntArray(2).apply { getLocationInWindow(this) }

val Widget.locationOnScreen
  get () = IntArray(2).apply { getLocationOnScreen(this) }
//</editor-fold>

//<editor-fold desc="ViewPropertyAnimator">
fun ViewPropertyAnimator.duration(millis: Long) = this.setDuration(millis)

fun ViewPropertyAnimator.startDelay(millis: Long) = this.setStartDelay(millis)

fun ViewPropertyAnimator.interpolator(interpolator: TimeInterpolator) = this.setInterpolator(interpolator)

fun ViewPropertyAnimator.setListenerBy(onStart: (Animator?) -> (Unit) = animatorListenerCallbackStub,
    onEnd: (Animator?) -> (Unit) = animatorListenerCallbackStub, onCancel: (Animator?) -> (Unit) = animatorListenerCallbackStub,
    onRepeat: (Animator?) -> (Unit) = animatorListenerCallbackStub) = this.setListener(object : AnimatorListener {
  override fun onAnimationRepeat(animation: Animator?) = onRepeat(animation)
  override fun onAnimationEnd(animation: Animator?) = onEnd(animation)
  override fun onAnimationCancel(animation: Animator?) = onCancel(animation)
  override fun onAnimationStart(animation: Animator?) = onStart(animation)
})
//</editor-fold>

//<editor-fold desc="Animation and AnimationSet">
fun Animation.setListenerBy(onStart: (Animation?) -> (Unit) = animationListenerCallbackStub,
    onEnd: (Animation?) -> (Unit) = animationListenerCallbackStub,
    onRepeat: (Animation?) -> (Unit) = animationListenerCallbackStub) = this.setAnimationListener(object : AnimationListener {
  override fun onAnimationRepeat(animation: Animation?) = onRepeat(animation)
  override fun onAnimationEnd(animation: Animation?) = onEnd(animation)
  override fun onAnimationStart(animation: Animation?) = onStart(animation)
})


fun AnimationSet.attach(targetView: View) {
  targetView.animation = this
}
//</editor-fold>

fun createImageViewWithDrawable(context: Context, drawable: Drawable?) = ImageView(context).apply { setImageDrawable(drawable) }

//todo: make more extensions functions like this - extension val
val View.idName: String
  get() = if (id != View.NO_ID) resources.getResourceEntryName(id) else noIdString()

internal fun View.visibilityAsString() = when (visibility) {
  View.VISIBLE   -> "visible"
  View.INVISIBLE -> "invisible"
  View.GONE      -> "gone"
  else           -> "UNKNOWN"
}

typealias ViewAction = (View) -> (Unit)
fun ViewGroup.addView(inflater: LayoutInflater, @LayoutRes layoutResource: Int, applyAction: ViewAction? = null) =
  addView(inflater.inflate(layoutResource, this, false).apply { applyAction?.invoke(this) })

fun View.generateId() {
  id = hashCode().absoluteValue
}
