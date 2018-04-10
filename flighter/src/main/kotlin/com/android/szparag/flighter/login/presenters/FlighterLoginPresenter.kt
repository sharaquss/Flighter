package com.android.szparag.flighter.login.presenters

import com.android.szparag.flighter.login.interactors.LoginInteractor
import com.android.szparag.flighter.login.states.LoginViewState
import com.android.szparag.flighter.login.states.LoginViewState.OnboardingLoginViewState
import com.android.szparag.flighter.login.states.LoginViewState.OnboardingRegisterViewState
import com.android.szparag.flighter.login.views.LoginView
import com.android.szparag.mvi.presenters.BaseMviPresenter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/04/2018.
 */
@Singleton
class FlighterLoginPresenter @Inject constructor(
    override var model: LoginInteractor) : BaseMviPresenter<LoginView, LoginInteractor, LoginViewState>(), LoginPresenter {

  init {
    Timber.d("init")
  }

  override fun onFirstViewAttached() {
    Timber.d("onFirstViewAttached, view: $view")
    requireNotNull(view)

    model
        .isUserRegistered()
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe {
          model.checkIfUserRegistered()
        }
        .subscribe { registered ->
          Timber.d("model.isUserRegistered().onNext, registered: $registered")
          view?.render(if (registered) OnboardingLoginViewState() else OnboardingRegisterViewState())
        }

    processLoginRegisterIntents()
    processSkipIntents()
    processDialogAcceptanceIntents()
    processDialogDismissalIntents()

//    Completable.timer(100, MILLISECONDS).subscribe { model.checkIfUserRegistered() }
  }

  private fun processLoginRegisterIntents() {
    Timber.d("processLoginRegisterIntents")
    view?.let {
      it.loginRegisterIntent()
          .subscribeOn(AndroidSchedulers.mainThread())
          .observeOn(Schedulers.single())
          .subscribe { intent ->
            Timber.d("processLoginRegisterIntents.onNext, intent: $intent")
          }
    }
  }

  private fun processSkipIntents() {
    Timber.d("processSkipIntents")
    view?.let {
      it.skipIntent()
          .subscribeOn(AndroidSchedulers.mainThread())
          .observeOn(Schedulers.single())
          .subscribe { intent ->
            Timber.d("processSkipIntents.onNext, intent: $intent")
          }
    }
  }

  private fun processDialogAcceptanceIntents() {
    Timber.d("processDialogAcceptanceIntents")
    view?.let {
      it.dialogAcceptanceIntent()
          .subscribeOn(AndroidSchedulers.mainThread())
          .observeOn(Schedulers.single())
          .subscribe { intent ->
            Timber.d("processDialogAcceptanceIntents.onNext, intent: $intent")
          }
    }
  }

  private fun processDialogDismissalIntents() {
    Timber.d("processDialogDismissalIntents")
    view?.let {
      it.dialogDismissalIntent()
          .subscribeOn(AndroidSchedulers.mainThread())
          .observeOn(Schedulers.single())
          .subscribe { intent ->
            Timber.d("processDialogDismissalIntents, intent: $intent")
            model.checkIfUserRegistered()
          }
    }
  }

}