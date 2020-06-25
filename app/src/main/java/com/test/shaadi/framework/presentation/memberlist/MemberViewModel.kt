package com.test.shaadi.framework.presentation.memberlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberViewModel
@Inject
constructor(private val memberSyncManager: MemberSyncManager) : ViewModel() {

    init {
        syncCacheWithNetwork()
    }

    fun hasSyncBeenExecuted() = memberSyncManager.hasSyncBeenExecuted

    private fun syncCacheWithNetwork() {
        memberSyncManager.executeDataSync(viewModelScope)
    }
}