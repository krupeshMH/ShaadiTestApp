package com.test.shaadi.framework.presentation.memberlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.shaadi.R
import com.test.shaadi.business.usecases.GetMembers
import com.test.shaadi.framework.presentation.common.BaseFragment
import com.test.shaadi.framework.presentation.memberlist.state.MemberListStateEvent
import kotlinx.android.synthetic.main.fragment_members.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeRefresh()
        subscribeObservers()
    }

    private fun setupUI() {
        //view?.hideKeyboard()
    }

    private fun setupSwipeRefresh() {
        swipe_refresh.setOnRefreshListener {
            reloadMembers()
            swipe_refresh.isRefreshing = false
        }
    }

    private fun reloadMembers() {
        TODO("Not yet implemented")
    }

    private fun subscribeObservers() {
        swipe_refresh.isRefreshing = true
        viewModel.hasSyncBeenExecuted()
            .observe(viewLifecycleOwner, Observer { hasSyncBeenExecuted ->
                swipe_refresh.isRefreshing = false
                if (hasSyncBeenExecuted) {
                    // load from cache and update ui
                    loadCachedMembers()
                }
            })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.memberList.let {

                }

                viewState.is_accepted.let {

                }
            }
        })
    }

    private fun loadCachedMembers() {
        viewModel.setStateEvent(
            MemberListStateEvent.GetMembersEvent()
        )
    }
}