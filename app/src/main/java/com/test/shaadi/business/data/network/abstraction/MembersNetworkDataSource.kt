package com.test.shaadi.business.data.network.abstraction

import com.test.shaadi.business.domain.model.Members

interface MembersNetworkDataSource {

    suspend fun getAllMembers(): Members

}