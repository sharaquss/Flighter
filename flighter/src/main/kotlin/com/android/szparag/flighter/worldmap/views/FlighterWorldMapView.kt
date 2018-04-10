package com.android.szparag.flighter.worldmap.views

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.flighter.R.raw
import com.android.szparag.flighter.common.util.ActivityLifecycleState
import com.android.szparag.flighter.common.util.ActivityLifecycleState.*
import com.android.szparag.flighter.common.util.Injector
import com.android.szparag.flighter.worldmap.presenters.WorldMapPresenter
import com.android.szparag.flighter.worldmap.states.WorldMapViewState
import com.android.szparag.flighter.worldmap.states.WorldMapViewState.ErrorViewState
import com.android.szparag.flighter.worldmap.states.WorldMapViewState.InteractiveViewState
import com.android.szparag.flighter.worldmap.states.WorldMapViewState.OnboardingViewState
import com.android.szparag.flighter.worldmap.states.WorldMapViewState.ShowingLocationViewState
import com.android.szparag.mvi.views.BaseMviMapView
import com.google.android.gms.maps.model.MapStyleOptions
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class FlighterWorldMapView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  BaseMviMapView<WorldMapViewState>(context, attrs, defStyleAttr), WorldMapView {

  @Inject @Suppress("MemberVisibilityCanBePrivate") lateinit var presenter: WorldMapPresenter
  @Volatile private var initialized = false
    set(value) {
      field = value
      mapInitializedSubject.onNext(value)
    }
  private val mapInitializedSubject = PublishSubject.create<Boolean>().apply {
    this.doOnSubscribe { this.onNext(initialized) } //todo: is it correctly implemented?
  }

  init {
    Timber.d("init")
    Injector.get().inject(this)
  }

  override fun mapInitializedIntent(): Observable<Boolean> = mapInitializedSubject

  override fun handleFirstRender(state: WorldMapViewState) {
    super.handleFirstRender(state)
    this.getMapAsync { googleMap ->
      Timber.d("init.getMapAsync.callback, googleMap: $googleMap")
      googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, raw.googlemapstyle))
      googleMap.uiSettings.apply {
        setAllGesturesEnabled(false)
      }
      initialized = true
    }
  }

  override fun render(state: WorldMapViewState) {
    super.render(state)
    Timber.d("render, state: $state")
    when (state) {
      is OnboardingViewState -> {
      }
      is ShowingLocationViewState -> {
      }
      is InteractiveViewState -> {
      }
      is ErrorViewState -> {
      }
    }
  }

  @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
  fun registerActivityStateObservable(activityStateObservable: Observable<ActivityLifecycleState>) {
    Timber.d("registerActivityStateObservable, activityStateObservable: $activityStateObservable")
    activityStateObservable.subscribe { state ->
      Timber.d("registerActivityStateObservable.onNext, state: $state")
      when (state) {
        ONCREATE -> onCreate(null)
        ONSTART -> onStart()
        ONRESUME -> onResume()
        ONPAUSE -> onPause()
        ONSTOP -> onStop()
        ONDESTROY -> onDestroy()
        ONLOWMMEMORY -> onLowMemory()
        ONSAVEINSTANCESTATE -> onSaveInstanceState(null)
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