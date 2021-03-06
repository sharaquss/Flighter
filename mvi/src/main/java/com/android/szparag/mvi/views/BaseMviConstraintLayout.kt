package com.android.szparag.mvi.views

import android.content.Context
import android.support.annotation.CallSuper
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import com.android.szparag.columbus.Navigator
import com.android.szparag.mvi.presenters.MviPresenter
import com.android.szparag.myextensionsandroid.hide
import com.android.szparag.myextensionsandroid.show
import com.szparag.android.mypermissions.PermissionRequestAction
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/04/2018.
 */
//todo: this and BaseMviMapView shares too much code, co something with that
abstract class BaseMviConstraintLayout<in V : MviView<VS>, P : MviPresenter<V, VS>, in VS : Any>
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
  : ConstraintLayout(context, attrs, defStyleAttr), MviView<VS> {

  abstract var presenter: P
  protected val viewDisposables = CompositeDisposable()
  private var firstStateRendered = false
  override lateinit var navigationDelegate: Navigator
  override lateinit var permissionRequestAction: PermissionRequestAction

  init {
    if (!isInEditMode) hide()
  }

  abstract fun instantiatePresenter()

  @Suppress("UNCHECKED_CAST")
  @CallSuper
  open fun attachToPresenter() {
    presenter.attachView(this as V)
  }

  @Suppress("UNCHECKED_CAST")
  @CallSuper
  open fun detachFromPresenter() {
    presenter.detachView(this as V)
  }

  @CallSuper
  override fun render(state: VS) { //todo: this should be abstract, this is mandatory to implement
    if (!firstStateRendered) {
      handleFirstRender(state)
    }
  }

  protected open fun handleFirstRender(state: VS) {
    navigationDelegate.onHandleFirstRender(getScreen())
    show()
    firstStateRendered = true
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    if (isInEditMode) return
    viewDisposables.clear()
    instantiatePresenter()
    attachToPresenter()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    if (isInEditMode) return
    detachFromPresenter()
    viewDisposables.clear()
  }

}