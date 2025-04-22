package com.hpcreation.mapComposeDemo.di

import com.hpcreation.mapComposeDemo.data.MarkerRepository
import com.hpcreation.mapComposeDemo.data.MarkerRepositoryImpl
import com.hpcreation.mapComposeDemo.viewmodel.MapViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Provide the implementation of MarkerRepository
    single<MarkerRepository> { MarkerRepositoryImpl() }

    // Provide MapViewModel
    viewModel { MapViewModel(get()) }
}