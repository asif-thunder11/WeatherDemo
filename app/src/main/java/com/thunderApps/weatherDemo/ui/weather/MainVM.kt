package com.thunderApps.weatherDemo.ui.weather

import androidx.lifecycle.ViewModel
import com.thunderApps.weatherDemo.domain.repository.LocalRepositorySource
import com.thunderApps.weatherDemo.domain.repository.RemoteRepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    val localRepo: LocalRepositorySource,
    val remoteRepo: RemoteRepositorySource
) : ViewModel() {



}