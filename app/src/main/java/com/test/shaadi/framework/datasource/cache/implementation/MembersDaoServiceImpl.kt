package com.test.shaadi.framework.datasource.cache.implementation

import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.model.Members
import com.test.shaadi.framework.datasource.cache.abstraction.MembersDaoService
import com.test.shaadi.framework.datasource.cache.database.MemberDao
import com.test.shaadi.framework.datasource.mappers.CacheMapper

class MembersDaoServiceImpl
constructor(
    private val dao: MemberDao,
    private val memberMapper: CacheMapper
) : MembersDaoService {
    override suspend fun insertAllMembers(member: Members): LongArray {
        return dao.insertMembers(memberMapper.memberListToEntityList(member.results))
    }

    override suspend fun insertMember(member: MemberEach): Long {
        return dao.insertMember(memberMapper.mapToEntity(member))
    }

    override suspend fun updateMember(member: MemberEach, is_accepted: Int): Int {
        return dao.updateMember(
            member.login.uuid,
            is_accepted
        )
    }

    override suspend fun getAllMembers(): List<MemberEach> {
        return memberMapper.entityListToMemberList(dao.getAllMember())
    }

    override suspend fun searchMemberById(id: String): MemberEach {
        return dao.searchMemberById(id)?.let { memberMapper.mapFromEntity(it) }!!
    }

}