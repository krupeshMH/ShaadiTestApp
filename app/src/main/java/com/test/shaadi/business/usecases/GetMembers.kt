package com.test.shaadi.business.usecases

import com.test.shaadi.business.data.cache.CacheResponseHandler
import com.test.shaadi.business.data.cache.abstraction.MembersCacheDataSource
import com.test.shaadi.business.data.network.ApiResponseHandler
import com.test.shaadi.business.data.network.abstraction.MembersNetworkDataSource
import com.test.shaadi.business.data.util.safeApiCall
import com.test.shaadi.business.data.util.safeCacheCall
import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.model.Members
import com.test.shaadi.business.domain.state.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMembers
@Inject
constructor(
    private val membersCacheDataSource: MembersCacheDataSource,
    private val membersNetworkDataSource: MembersNetworkDataSource
) {

    suspend fun syncData() {
        val networkNotesObject = getMembersFromNetwork()
        syncNetworkDataWithCache(networkNotesObject)
    }

    private suspend fun getCachedMembers(): List<MemberEach> {
        val cacheResult = safeCacheCall(Dispatchers.IO) {
            membersCacheDataSource.getAllMembers()
        }

        val response =
            object : CacheResponseHandler<List<MemberEach>, List<MemberEach>>(
                response = cacheResult,
                stateEvent = null
            ) {
                override suspend fun handleSuccess(resultObj: List<MemberEach>): DataState<List<MemberEach>>? {
                    return DataState.data(
                        response = null,
                        data = resultObj,
                        stateEvent = null
                    )
                }

            }.getResult()

        return response?.data!!
    }

    private suspend fun getMembersFromNetwork(): Members {
        val networkResult = safeApiCall(Dispatchers.IO) {
            membersNetworkDataSource.getAllMembers()
        }

        val response = object : ApiResponseHandler<Members, Members>(
            response = networkResult,
            stateEvent = null
        ) {
            override suspend fun handleSuccess(resultObj: Members): DataState<Members>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data!!
    }

    private suspend fun syncNetworkDataWithCache(
        members: Members
    ) = withContext(Dispatchers.IO) {

        for (eachMember in members.results) {
            membersCacheDataSource.insertMember(eachMember)
        }

        val list = membersCacheDataSource.getAllMembers()
        for (eachEntity in list) {
            println(eachEntity.name)
        }
    }
}