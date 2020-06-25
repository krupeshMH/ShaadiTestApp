package com.test.shaadi.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.shaadi.framework.datasource.model.MemberCacheEntity

@Dao
interface MemberDao {

    @Insert
    suspend fun insertMember(member: MemberCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMembers(members: List<MemberCacheEntity>): LongArray

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun searchMemberById(id: String): MemberCacheEntity?

    @Query("SELECT * FROM members")
    suspend fun getAllMember(): List<MemberCacheEntity>

    @Query(
        """
        UPDATE members 
        SET 
        name = :name, 
        location = :location,
        dob = :dob,
        accepted = :is_accepted
        WHERE id = :primaryKey
        """
    )
    suspend fun updateMember(
        primaryKey: String,
        name: String,
        location: String?,
        dob: String,
        is_accepted: Int
    ): Int

    @Query("SELECT * FROM members")
    suspend fun getAllNotes(): List<MemberCacheEntity>
}