package com.jugal.trainingsample.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.jugal.trainingsample.R
import com.jugal.trainingsample.data.db.dao.PeoplesDao
import com.jugal.trainingsample.data.db.schema.Peoples
import com.jugal.trainingsample.data.event.NetworkEvent
import com.jugal.trainingsample.data.model.PeopleRemoteData
import com.jugal.trainingsample.data.model.PeopleRemoteDetail
import com.jugal.trainingsample.data.model.toPeoplesEntity
import com.jugal.trainingsample.data.networking.api.PeopleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private class PeopleRepositoryImpl(
    private val api: PeopleApi,
    private val dao: PeoplesDao
) : PeopleRepository {

    private val _networkEvent = MutableLiveData<NetworkEvent>()
    override val networkEvents: LiveData<NetworkEvent>
        get() = _networkEvent

    override suspend fun getPeoplesList(page: Int, pageSize: Int): LiveData<PeopleRemoteData> {
        return liveData(Dispatchers.IO) {
            try {
                _networkEvent.postValue(NetworkEvent.Loading)
                val response = api.getPeoplesList(page, pageSize)
                if (response.isSuccessful && response.code() == 200) {
                    withContext(Dispatchers.Main) {
                        _networkEvent.postValue(NetworkEvent.Success)
                    }
                    val data = response.body() ?: throw IllegalStateException("Body is null")
                    emit(data)

                    if (!data.peopleList.isNullOrEmpty())
                        dao.insertPeopleList(
                            *data.peopleList.toPeoplesEntity().toTypedArray()
                        )
                } else {
                    throw IllegalStateException(
                        "code: ${response.code()}, body: ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _networkEvent.postValue(
                        NetworkEvent.Failure(R.string.error_generic)
                    )
                }
            }
        }
    }

    override suspend fun getPeoplesListFromDb(): List<Peoples>? {
        return withContext(Dispatchers.IO) {
            dao.getAll()
        }
    }

    override suspend fun getPeopleDetails(url: String): LiveData<PeopleRemoteDetail> {
        _networkEvent.postValue(NetworkEvent.Loading)
        return liveData(Dispatchers.IO) {
            try {
                val response = api.getPeopleDetails(url).run {
                    if (this.isSuccessful) {
                        this.body() ?: throw IllegalStateException("Body is null")
                    } else {
                        throw IllegalStateException(
                            "code: ${this.code()}, body: ${
                                this.errorBody()?.string()
                            }"
                        )
                    }
                }
                emit(response)
                _networkEvent.postValue(NetworkEvent.Success)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _networkEvent.postValue(
                        NetworkEvent.Failure(R.string.error_generic)
                    )
                }
            }
        }
    }
}

interface PeopleRepository {

    val networkEvents: LiveData<NetworkEvent>

    suspend fun getPeoplesList(page: Int, pageSize: Int): LiveData<PeopleRemoteData>

    suspend fun getPeopleDetails(url: String): LiveData<PeopleRemoteDetail>

    suspend fun getPeoplesListFromDb(): List<Peoples>?

    companion object Factory {
        fun create(api: PeopleApi, dao: PeoplesDao): PeopleRepository {
            return PeopleRepositoryImpl(api, dao)
        }
    }

}