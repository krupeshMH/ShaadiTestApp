package com.test.shaadi.framework.presentation.common

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.shaadi.business.usecases.MemberListInteractors
import com.test.shaadi.framework.presentation.memberlist.MemberSyncManager
import com.test.shaadi.framework.presentation.memberlist.MemberViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class MemberViewModelFactory
@Inject
constructor(
    private val memberSyncManager: MemberSyncManager,
    private val interactors: MemberListInteractors
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MemberViewModel::class.java -> {
                MemberViewModel(
                    memberSyncManager = memberSyncManager,
                    interactors = interactors
                ) as T
            }
            else -> {
                throw IllegalArgumentException("unknown model class $modelClass")
            }
        }
    }
}