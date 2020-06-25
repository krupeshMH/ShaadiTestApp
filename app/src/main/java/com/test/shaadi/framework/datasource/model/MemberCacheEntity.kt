package com.test.shaadi.framework.datasource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.shaadi.business.domain.model.*

@Entity(tableName = "members")
data class MemberCacheEntity(

    @ColumnInfo(name = "cell")
    var cell: String,

    @ColumnInfo(name = "dob")
    var dob: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "gender")
    var gender: String,

    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "location")
    var location: String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uuid")
    var uuid: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "nationality")
    var nat: String,

    @ColumnInfo(name = "phone")
    var phone: String,

    @ColumnInfo(name = "picture")
    var picture: String,

    @ColumnInfo(name = "accepted")
    var is_accepted: Int

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberCacheEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}