package com.test.shaadi.framework.presentation.memberlist.state

import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.state.StateEvent

sealed class MemberListStateEvent : StateEvent {

    class AcceptDeclineEvent(
        val is_accepted: Int,
        val member: MemberEach
    ) : MemberListStateEvent() {

        override fun errorInfo(): String {
            return "Error accepting/rejecting."
        }

        override fun eventName(): String {
            return "AcceptDeclineEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class GetMembersEvent : MemberListStateEvent() {

        override fun errorInfo(): String {
            return "Error loading."
        }

        override fun eventName(): String {
            return "Loaded all cached members"
        }

        override fun shouldDisplayProgressBar() = true
    }
}