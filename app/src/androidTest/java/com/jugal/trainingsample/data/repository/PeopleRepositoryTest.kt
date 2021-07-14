package com.jugal.trainingsample.data.repository

import com.jugal.trainingsample.R
import com.jugal.trainingsample.utils.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import javax.net.ssl.HttpsURLConnection

@ExperimentalCoroutinesApi
class PeopleRepositoryTest : RepositoryTest<PeopleRepository>() {

    override val repository: PeopleRepository
        get() = PeopleRepository.create(
            createApi(url), database.peoplesDao()
        )

    @Test
    fun getPeoplesList() {
        mockHttpResponse(R.raw.people_list, HttpsURLConnection.HTTP_OK)
        GlobalScope.launch(Dispatchers.IO) {
            repository.getPeoplesList(1, 6).observeForever {
                it.peopleList.size shouldBe 6
            }
        }
    }
}