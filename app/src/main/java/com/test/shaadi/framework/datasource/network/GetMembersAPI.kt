package com.test.shaadi.framework.datasource.network

import com.test.shaadi.business.domain.model.Members
import retrofit2.http.GET

interface GetMembersAPI {
    @GET("api/?results=10")
    suspend fun getMembers(): Members
}