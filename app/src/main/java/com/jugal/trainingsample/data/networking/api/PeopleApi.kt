package com.jugal.trainingsample.data.networking.api

import com.jugal.trainingsample.data.model.PeopleRemoteData
import com.jugal.trainingsample.data.model.PeopleRemoteDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PeopleApi {

    @GET(URL_PEOPLE_GET_LIST)
    suspend fun getPeoplesList(
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Response<PeopleRemoteData>

    @GET
    suspend fun getPeopleDetails(
        @Url url: String
    ): Response<PeopleRemoteDetail>

    companion object {
        const val URL_PEOPLE_GET_LIST = "users"
    }

}