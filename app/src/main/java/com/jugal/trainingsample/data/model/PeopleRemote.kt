package com.jugal.trainingsample.data.model

import android.os.Parcelable
import com.jugal.trainingsample.data.db.schema.Peoples
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PeopleRemoteData(
    @SerializedName("data") val peopleList: List<PeopleRemote>
) : Parcelable

@Parcelize
data class PeopleRemote(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("avatar") val url: String
) : Parcelable {
    fun getFullName(): String {
        return "$firstName $lastName"
    }
}

fun List<PeopleRemote>.toPeoplesEntity(): List<Peoples> =
    this.map {
        Peoples(it.id, it.email, it.firstName, it.lastName, it.url)
    }

