package com.test.shaadi.framework.presentation.memberlist

import androidx.lifecycle.viewModelScope
import com.test.shaadi.business.domain.state.DataState
import com.test.shaadi.business.domain.state.StateEvent
import com.test.shaadi.business.usecases.MemberListInteractors
import com.test.shaadi.framework.presentation.common.BaseViewModel
import com.test.shaadi.framework.presentation.memberlist.state.MemberListStateEvent
import com.test.shaadi.framework.presentation.memberlist.state.MemberListViewState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberViewModel
@Inject
constructor(
    private val memberSyncManager: MemberSyncManager,
    private val interactors: MemberListInteractors
) :
    BaseViewModel<MemberListViewState>() {

    init {
        syncCacheWithNetwork()
    }

    fun hasSyncBeenExecuted() = memberSyncManager.hasSyncBeenExecuted

    private fun syncCacheWithNetwork() {
        memberSyncManager.executeDataSync(viewModelScope)
    }

    override fun handleNewData(data: MemberListViewState) {
        data.let { viewState ->
            viewState.memberList.let { memberList ->
                //setMemberListData(memberList)
                println(memberList)
            }

            viewState.is_accepted?.let { isAccepted ->
                println(isAccepted)

            }

        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<MemberListViewState>?> = when (stateEvent) {

            is MemberListStateEvent.GetMembersEvent -> {
                interactors.getMembers.getCachedMembers(
                    stateEvent = stateEvent
                )
            }

            is MemberListStateEvent.AcceptDeclineEvent -> {
                interactors.getMembers.updateMember(
                    stateEvent = stateEvent,
                    memberEach = stateEvent.member,
                    is_accepted = stateEvent.is_accepted
                )
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }

        launchJob(stateEvent, job)
    }

    override fun initNewViewState(): MemberListViewState {
        return MemberListViewState()
    }
}