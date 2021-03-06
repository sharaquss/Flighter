package com.android.szparag.flighter.common

import android.app.Application
import com.android.szparag.flighter.common.preferences.UserPreferencesModel
import com.google.firebase.FirebaseApp
import com.squareup.leakcanary.LeakCanary
import io.realm.Realm
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/04/2018.
 */
class FlighterApplication : Application() {

  companion object {
    lateinit var component: FlighterComponent
      private set
  }

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) { return }

    LeakCanary.install(this)
    Timber.uprootAll()
    Timber.plant(DebugTree())
    Timber.d("onCreate")
    component = DaggerFlighterComponent.builder().flighterGlobalModule(FlighterGlobalModule(applicationContext)).build()
    FirebaseApp.initializeApp(applicationContext)
    Realm.init(this)
    resetUserPreferences()
  }

  private fun resetUserPreferences() {
    with(Realm.getDefaultInstance()) {
      executeTransaction {
        copyToRealmOrUpdate(UserPreferencesModel())
      }
    }
  }


}