package com.test.shaadi.framework.datasource.cache.abstraction

import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.model.Members

interface MembersDaoService {
    suspend fun insertAllMembers(member: Members): LongArray

    suspend fun insertMember(member: MemberEach): Long

    suspend fun updateMember(member: MemberEach, is_accepted: Int): Int

    suspend fun getAllMembers(): List<MemberEach>

    suspend fun searchMemberById(id: String): MemberEach
}