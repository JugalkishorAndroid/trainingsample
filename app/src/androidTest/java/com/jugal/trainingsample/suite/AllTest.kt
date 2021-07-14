package com.jugal.trainingsample.suite

import com.jugal.trainingsample.data.networking.api.ApiSuite
import com.jugal.trainingsample.app.viewmodel.ViewModelSuite
import fr.appsolute.wellness.data.repository.RepositorySuite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    ApiSuite::class,
    ViewModelSuite::class,
    RepositorySuite::class
)
class AllTest