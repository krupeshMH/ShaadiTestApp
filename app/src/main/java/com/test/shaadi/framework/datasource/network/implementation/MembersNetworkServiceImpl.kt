package com.test.shaadi.framework.datasource.network.implementation

import com.test.shaadi.business.domain.model.Members
import com.test.shaadi.framework.datasource.network.GetMembersAPI
import com.test.shaadi.framework.datasource.network.abstraction.MembersNetworkService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MembersNetworkServiceImpl
@Inject
constructor(private val api: GetMembersAPI) : MembersNetworkService {
    override suspend fun getMemberData(): Members {
        return api.getMembers()
    }
}