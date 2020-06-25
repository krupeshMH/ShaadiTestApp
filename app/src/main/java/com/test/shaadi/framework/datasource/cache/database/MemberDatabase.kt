package com.test.shaadi.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.shaadi.framework.datasource.model.MemberCacheEntity

@Database(entities = [MemberCacheEntity::class], version = 1)
abstract class MemberDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao

    companion object {
        val DATABASE_NAME: String = "member_db"
    }


}