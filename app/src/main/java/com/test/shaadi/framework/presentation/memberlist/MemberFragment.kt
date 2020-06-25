package com.test.shaadi.framework.presentation.memberlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.shaadi.R
import com.test.shaadi.framework.presentation.common.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class MemberFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(R.layout.fragment_members) {
    private val viewModel: MemberViewModel by viewModels {
        viewModelFactory
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.hasSyncBeenExecuted()
            .observe(viewLifecycleOwner, Observer { hasSyncBeenExecuted ->

                if (hasSyncBeenExecuted) {

                }
            })
    }
}