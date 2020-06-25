package com.test.shaadi.di

import com.test.shaadi.framework.presentation.BaseApplication
import com.test.shaadi.framework.presentation.MainActivity
import com.test.shaadi.framework.presentation.memberlist.MemberFragment
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
@Component(
    modules = [
        AppModule::class,
        ProductionModule::class,
        MembersViewModelModule::class,
        AppFragmentFactoryModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance app: BaseApplication): AppComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(memberFragment: MemberFragment)
}