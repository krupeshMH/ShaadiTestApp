package com.test.shaadi.framework.presentation.memberlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.shaadi.business.usecases.GetMembers
import com.test.shaadi.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberSyncManager
@Inject
constructor(private val getMembers: GetMembers) {

    private val _hasSyncBeenExecuted: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasSyncBeenExecuted: LiveData<Boolean>
        get() = _hasSyncBeenExecuted

    fun executeDataSync(coroutineScope: CoroutineScope){
        if(_hasSyncBeenExecuted.value!!){
            return
        }

        val syncJob = coroutineScope.launch {
            val getMembersFromNetwork = launch {
                printLogD("syncData",
                    "syncing data.")
                getMembers.syncData()
            }
            getMembersFromNetwork.join()
        }

        syncJob.invokeOnCompletion {
            CoroutineScope(Dispatchers.Main).launch{
                _hasSyncBeenExecuted.value = true
            }
        }
    }
}