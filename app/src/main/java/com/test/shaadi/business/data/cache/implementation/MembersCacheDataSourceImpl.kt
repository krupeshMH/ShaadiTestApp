package com.test.shaadi.business.data.cache.implementation

import com.test.shaadi.business.data.cache.abstraction.MembersCacheDataSource
import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.model.Members
import com.test.shaadi.framework.datasource.cache.abstraction.MembersDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MembersCacheDataSourceImpl
@Inject
constructor(private val membersDaoService: MembersDaoService) : MembersCacheDataSource {

    override suspend fun insertAllMembers(members: Members): LongArray {
        return membersDaoService.insertAllMembers(members)
    }

    override suspend fun insertMember(member: MemberEach): Long {
        return membersDaoService.insertMember(member)
    }

    override suspend fun updateMember(member: MemberEach, value: Int): Int {
        return membersDaoService.updateMember(member, value)
    }

    override suspend fun getAllMembers(): List<MemberEach> {
        return membersDaoService.getAllMembers()
    }

    override suspend fun searchMemberById(id: String): MemberEach {
        return membersDaoService.searchMemberById(id)
    }

}