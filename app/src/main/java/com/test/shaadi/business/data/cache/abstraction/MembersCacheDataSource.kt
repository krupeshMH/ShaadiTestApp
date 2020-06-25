package com.test.shaadi.business.data.cache.abstraction

import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.model.Members

interface MembersCacheDataSource {

    suspend fun insertAllMembers(member: Members): LongArray

    suspend fun insertMember(member: MemberEach): Long

    suspend fun updateMember(member: MemberEach,value:Int): Int

    suspend fun getAllMembers(): List<MemberEach>

    suspend fun searchMemberById(id:String): MemberEach

}