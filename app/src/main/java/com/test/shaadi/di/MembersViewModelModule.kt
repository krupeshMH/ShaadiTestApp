package com.test.shaadi.di

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.test.shaadi.framework.presentation.common.MemberViewModelFactory
import com.test.shaadi.framework.presentation.memberlist.MemberSyncManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Module
object MembersViewModelModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideMemberViewModelFactory(
        memberSyncManager: MemberSyncManager
    ): ViewModelProvider.Factory {
        return MemberViewModelFactory(
            memberSyncManager = memberSyncManager
        )
    }

}