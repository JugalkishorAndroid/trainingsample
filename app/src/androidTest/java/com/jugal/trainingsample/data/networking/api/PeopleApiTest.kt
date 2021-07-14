package com.jugal.trainingsample.data.networking.api

import com.jugal.trainingsample.R
import com.jugal.trainingsample.utils.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.net.ssl.HttpsURLConnection

class PeopleApiTest : ApiTest<PeopleApi>() {
    override val api: PeopleApi
        get() = createApi(url)

    @Test
    fun getPeoplesList() = runBlocking(Dispatchers.IO) {
        mockHttpResponse(R.raw.people_list, HttpsURLConnection.HTTP_OK)
        api.getPeoplesList(1, 6).body()?.peopleList?.size shouldBe 6
    }
}
