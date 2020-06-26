package com.test.shaadi.business.usecases

import com.test.shaadi.business.data.cache.CacheResponseHandler
import com.test.shaadi.business.data.cache.abstraction.MembersCacheDataSource
import com.test.shaadi.business.data.network.ApiResponseHandler
import com.test.shaadi.business.data.network.abstraction.MembersNetworkDataSource
import com.test.shaadi.business.data.util.safeApiCall
import com.test.shaadi.business.data.util.safeCacheCall
import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.model.Members
import com.test.shaadi.business.domain.state.*
import com.test.shaadi.framework.presentation.memberlist.state.MemberListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        val networkMembersObject = getMembersFromNetwork()
        syncNetworkDataWithCache(networkMembersObject)
    }

    fun getCachedMembers(stateEvent: StateEvent): Flow<DataState<MemberListViewState>?> =
        flow {
            val cacheResult = safeCacheCall(Dispatchers.IO) {
                membersCacheDataSource.getAllMembers()
            }

            val response =
                object : CacheResponseHandler<MemberListViewState, List<MemberEach>>(
                    response = cacheResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: List<MemberEach>): DataState<MemberListViewState>? {
                        val viewState = MemberListViewState(
                            memberList = resultObj
                        )
                        return DataState.data(
                            response = Response(
                                message = "Retrieved from local cache...",
                                uiComponentType = UIComponentType.None(),
                                messageType = MessageType.Success()
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                        )
                    }

                }.getResult()

            emit(response)
        }

    fun updateMember(
        stateEvent: StateEvent,
        memberEach: MemberEach,
        is_accepted: Int
    ): Flow<DataState<MemberListViewState>?> =
        flow {
            val cacheResult = safeCacheCall(Dispatchers.IO) {
                membersCacheDataSource.updateMember(memberEach, is_accepted)
            }

            val response =
                object : CacheResponseHandler<MemberListViewState, Int>(
                    response = cacheResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: Int): DataState<MemberListViewState>? {
                        val viewState = MemberListViewState(
                            is_accepted = resultObj
                        )
                        return DataState.data(
                            response = Response(
                                message = "Data updated",
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                        )
                    }

                }.getResult()

            emit(response)
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
                    response = Response(
                        message = "Data loaded",
                        uiComponentType = UIComponentType.Toast(),
                        messageType = MessageType.Success()
                    ),
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
    }
}