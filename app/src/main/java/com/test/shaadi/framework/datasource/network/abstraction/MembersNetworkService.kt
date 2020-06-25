package com.test.shaadi.framework.datasource.network.abstraction

import com.test.shaadi.business.domain.model.Members

interface MembersNetworkService {
    suspend fun getMemberData(): Members
}