package com.jugal.trainingsample.data.model

import com.google.gson.annotations.SerializedName

data class PeopleRemoteDetail(
    @SerializedName("data") val people: PeopleDetail,
    @SerializedName("ad") val companyDetail: CompanyDetail
)

data class PeopleDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("avatar") val url: String
) {
    fun getFullName(): String {
        return "$firstName $lastName"
    }
}

data class CompanyDetail(
    @SerializedName("company") val company: String,
    @SerializedName("url") val url: String,
    @SerializedName("text") val text: String
)