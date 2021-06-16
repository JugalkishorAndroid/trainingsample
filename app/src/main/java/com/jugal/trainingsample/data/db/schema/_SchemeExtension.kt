package com.jugal.trainingsample.data.db.schema

import com.jugal.trainingsample.data.model.PeopleRemote


fun List<Peoples>.toPeoplesRemoteEntity(): List<PeopleRemote> =
    this.map {
        PeopleRemote(it.id, it.email, it.firstName, it.lastName, it.avatar)
    }
