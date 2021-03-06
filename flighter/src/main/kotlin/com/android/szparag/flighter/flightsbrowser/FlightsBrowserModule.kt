package com.android.szparag.flighter.flightsbrowser

import com.android.szparag.flighter.flightsbrowser.interactors.FlighterFlightsBrowserInteractor
import com.android.szparag.flighter.flightsbrowser.interactors.FlightsBrowserInteractor
import com.android.szparag.flighter.flightsbrowser.presenters.FlighterFlightsBrowserPresenter
import com.android.szparag.flighter.flightsbrowser.presenters.FlightsBrowserPresenter
import com.android.szparag.flighter.flightsbrowser.states.FlightsBrowserViewState
import com.android.szparag.mvi.models.BaseMviModelRepository
import com.android.szparag.mvi.models.ModelRepository
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

private const val RYANAIR_API_BASE_URL = "http://apigateway.ryanair.com/pub/v1/farefinder/3/oneWayFares"

@Module
class FlightsBrowserModule {

  @Provides
  fun providePresenter(implementation: FlighterFlightsBrowserPresenter): FlightsBrowserPresenter = implementation

  @Provides
  fun provideInteractor(implementation: FlighterFlightsBrowserInteractor): FlightsBrowserInteractor = implementation

  @Provides
  @Singleton
  fun provideWorldMapViewStateDistributor(): ModelRepository<FlightsBrowserViewState> = BaseMviModelRepository()

//  @Provides
//  @Singleton
//  fun provideRyanairRetrofitInstance() = Retrofit.Builder()
//      .baseUrl(RYANAIR_API_BASE_URL)
//      .addConverterFactory(GsonConverterFactory.create(Gson()))
//
//  @Provides
//  @Singleton
//  fun provide

  @Provides
  @Singleton
  fun provideSimpleDateFormat() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

}