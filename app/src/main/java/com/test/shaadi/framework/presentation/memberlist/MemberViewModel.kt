package com.test.shaadi.framework.presentation.memberlist

import androidx.lifecycle.viewModelScope
import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.state.DataState
import com.test.shaadi.business.domain.state.StateEvent
import com.test.shaadi.business.usecases.MemberListInteractors
import com.test.shaadi.framework.presentation.common.BaseViewModel
import com.test.shaadi.framework.presentation.memberlist.state.MemberListStateEvent
import com.test.shaadi.framework.presentation.memberlist.state.MemberListViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
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
                setMemberListData(memberList)
            }

            viewState.is_accepted?.let { isAccepted ->
                //println(isAccepted)
                isAcceptedDataUpdated(isAccepted)
            }

        }
    }

    private fun setMemberListData(memberList: List<MemberEach>?) {
        val update = getCurrentViewStateOrNew()
        update.memberList = memberList
        setViewState(update)
    }

    private fun isAcceptedDataUpdated(isAccepted: Int) {
        val update = getCurrentViewStateOrNew()
        update.is_accepted = isAccepted
        setViewState(update)
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