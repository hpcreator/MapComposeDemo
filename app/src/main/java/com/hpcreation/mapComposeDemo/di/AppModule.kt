package com.hpcreation.mapComposeDemo.di

import com.hpcreation.mapComposeDemo.data.repo.MarkerRepository
import com.hpcreation.mapComposeDemo.data.repo_impl.MarkerRepositoryImpl
import com.hpcreation.mapComposeDemo.utils.ApiKeyProvider
import com.hpcreation.mapComposeDemo.viewmodel.DirectionViewmodel
import com.hpcreation.mapComposeDemo.viewmodel.MapViewModel
import com.hpcreation.mapComposeDemo.viewmodel.AdvanceDataViewmodel
import com.hpcreation.mapComposeDemo.viewmodel.PolylineViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Provide the implementation of MarkerRepository
    single<MarkerRepository> { MarkerRepositoryImpl() }
    single { ApiKeyProvider(context = get()) }

    // Provide MapViewModel
    viewModel { MapViewModel(get()) }
    viewModel { PolylineViewmodel(get()) }
    viewModel { DirectionViewmodel(get(), get()) }
    viewModel { AdvanceDataViewmodel(get(), get()) }
}