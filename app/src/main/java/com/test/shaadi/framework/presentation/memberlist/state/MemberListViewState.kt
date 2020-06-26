package com.test.shaadi.framework.presentation.memberlist.state

import android.os.Parcelable
import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.state.ViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MemberListViewState(

    var memberList: List<MemberEach>? = null,

    var is_accepted : Int? = null

) : Parcelable, ViewState {

}