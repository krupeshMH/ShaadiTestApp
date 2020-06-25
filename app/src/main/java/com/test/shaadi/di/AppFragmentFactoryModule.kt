package com.test.shaadi.di

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.test.shaadi.framework.presentation.common.AppFragmentFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Module
object AppFragmentFactoryModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideAppFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return AppFragmentFactory(
            viewModelFactory
        )
    }
}