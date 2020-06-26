package com.test.shaadi.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Members(
    val info: Info,
    val results: List<MemberEach>
) : Parcelable

@Parcelize
data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
) : Parcelable

@Parcelize
data class MemberEach(
    val cell: String,
    val dob: Dob,
    val email: String,
    val gender: String,
    val id: Id,
    val location: Location,
    val login: Login,
    val name: Name,
    val nat: String,
    val phone: String,
    val picture: Picture,
    val registered: Registered
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberEach

        if (login.uuid != other.login.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + login.hashCode()
        return result
    }
}

@Parcelize
data class Dob(
    val age: Int,
    val date: String
) : Parcelable

@Parcelize
data class Id(
    val name: String,
    val value: String
) : Parcelable

@Parcelize
data class Location(
    val city: String,
    val coordinates: Coordinates,
    val country: String,
    val postcode: String,
    val state: String,
    val street: Street,
    val timezone: Timezone
) : Parcelable

@Parcelize
data class Login(
    val md5: String,
    val password: String,
    val salt: String,
    val sha1: String,
    val sha256: String,
    val username: String,
    val uuid: String
) : Parcelable

@Parcelize
data class Name(
    val first: String,
    val last: String,
    val title: String
) : Parcelable

@Parcelize
data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String
) : Parcelable

@Parcelize
data class Registered(
    val age: Int,
    val date: String
) : Parcelable

@Parcelize
data class Coordinates(
    val latitude: String,
    val longitude: String
) : Parcelable

@Parcelize
data class Street(
    val name: String,
    val number: Int
) : Parcelable

@Parcelize
data class Timezone(
    val description: String,
    val offset: String
) : Parcelable