package com.android.szparag.flighter.selectdeparture.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.android.szparag.flighter.R
import com.android.szparag.flighter.R.layout
import com.android.szparag.flighter.common.util.Injector
import com.android.szparag.flighter.selectdeparture.presenters.SelectDeparturePresenter
import com.android.szparag.flighter.selectdeparture.states.SelectDepartureViewState
import com.android.szparag.flighter.selectdeparture.states.SelectDepartureViewState.SearchNotStartedViewState
import com.android.szparag.kotterknife.bindView
import com.android.szparag.mvi.navigator.Screen
import com.android.szparag.mvi.views.BaseMviConstraintLayout
import com.android.szparag.myextensionsandroid.show
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/04/2018.
 */
class FlighterSelectDepartureView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : BaseMviConstraintLayout<SelectDepartureViewState>(context, attrs, defStyleAttr), SelectDepartureView {

  companion object {
    val screenData by lazy {
      Screen(
          viewClass = FlighterSelectDepartureView::class.java,
          layoutResource = layout.screen_select_departure
      )
    }
  }

  @Inject
  lateinit var presenter: SelectDeparturePresenter

  private val testTextView: TextView  by bindView(R.id.testTextView)

  init {
    Timber.d("init")
  }

  override fun render(state: SelectDepartureViewState) {
    super.render(state)
    Timber.d("render, state: $state")
    when (state) {
      is SearchNotStartedViewState -> {
        testTextView.show()
      }
    }
  }

  override fun instantiatePresenter() {
    Timber.d("instantiatePresenter")
    Injector.get().inject(this)
  }

  override fun attachToPresenter() {
    Timber.d("attachToPresenter")
    presenter.attachView(this)
  }

  override fun detachFromPresenter() {
    Timber.d("detachFromPresenter")
    presenter.detachView(this)
  }

}