package com.jugal.trainingsample.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.jugal.trainingsample.data.db.schema.Peoples
import com.jugal.trainingsample.data.model.PeopleRemoteData
import com.jugal.trainingsample.data.model.PeopleRemoteDetail
import com.jugal.trainingsample.data.repository.PeopleRepository
import kotlinx.coroutines.launch

class PeopleViewModel(
    private val repository: PeopleRepository
) : ViewModel() {

    val networkEvents = repository.networkEvents

    fun getPeoplesList(page: Int, pageSize: Int): LiveData<PeopleRemoteData> {
        return liveData {
            emitSource(repository.getPeoplesList(page,pageSize))
        }
    }

    fun getPeopleDetails(peopleUrl: String): LiveData<PeopleRemoteDetail> {
        return liveData {
            emitSource(repository.getPeopleDetails(peopleUrl))
        }
    }

    fun getPeopleListOffline(success: (peoplesList: List<Peoples>) -> Unit) {
        viewModelScope.launch {
            try {
                val peoplesList = repository.getPeoplesListFromDb() ?: mutableListOf()
                success(peoplesList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}