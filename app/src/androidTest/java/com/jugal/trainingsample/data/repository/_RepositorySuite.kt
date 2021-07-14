package fr.appsolute.wellness.data.repository

import com.jugal.trainingsample.data.repository.PeopleRepositoryTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    PeopleRepositoryTest::class
)
class RepositorySuite
