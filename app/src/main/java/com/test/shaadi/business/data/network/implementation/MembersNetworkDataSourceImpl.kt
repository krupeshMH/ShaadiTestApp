package com.test.shaadi.business.data.network.implementation

import com.test.shaadi.business.data.network.abstraction.MembersNetworkDataSource
import com.test.shaadi.business.domain.model.Members
import com.test.shaadi.framework.datasource.network.abstraction.MembersNetworkService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MembersNetworkDataSourceImpl
@Inject
constructor(
    private val service: MembersNetworkService
) : MembersNetworkDataSource {

    override suspend fun getAllMembers(): Members {
        return service.getMemberData()
    }

}