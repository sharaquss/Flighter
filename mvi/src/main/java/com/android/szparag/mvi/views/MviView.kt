package com.android.szparag.mvi.views

import com.android.szparag.mvi.navigator.Navigator

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/04/2018.
 */
interface MviView<in VS : Any> {

  var navigationDelegate: Navigator
  fun render(state: VS)

}