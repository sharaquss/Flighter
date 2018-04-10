package com.android.szparag.flighter.login.interactors

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlighterLoginInteractor @Inject constructor(): LoginInteractor {

  private var random = Random()
  private var registrationSubject = BehaviorSubject.create<Boolean>()

  init {
    Timber.d("init")
  }

  override fun checkIfUserRegistered() {
    Timber.d("checkIfUserRegistered")
    registrationSubject.onNext(random.nextBoolean())
  }

  override fun isUserRegistered(): Observable<Boolean> {
    Timber.d("isUserRegistered")
    return registrationSubject
  }

  override fun processRegistrationCredentials(email: String): Observable<Boolean> {
    Timber.d("processRegistrationCredentials, email: $email")
    return Observable.just(true)
  }
}