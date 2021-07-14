package com.jugal.trainingsample.app.viewmodel

import com.jugal.trainingsample.R
import com.jugal.trainingsample.data.repository.PeopleRepository
import com.jugal.trainingsample.utils.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import javax.net.ssl.HttpsURLConnection

@ExperimentalCoroutinesApi
class PeopleViewModelTest : ViewModelTest() {

    private lateinit var viewModel: PeopleViewModel

    override fun setUp() {
        super.setUp()
        viewModel = PeopleViewModel(
            PeopleRepository.create(
                createApi(url), database.peoplesDao()
            )
        )
    }

    @Test
    fun getPeoplesList() = runBlockingTest {
        mockHttpResponse(R.raw.people_list, HttpsURLConnection.HTTP_OK)
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.getPeoplesList(1, 10).observeForever {
                it.peopleList.size shouldBe 10
            }
        }
    }

    companion object {
        private const val TAG: String = "PeopleViewModelTest"
    }
}