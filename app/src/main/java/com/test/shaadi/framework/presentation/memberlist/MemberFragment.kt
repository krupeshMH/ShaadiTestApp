package com.test.shaadi.framework.presentation.memberlist

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.test.shaadi.R
import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.business.domain.state.StateMessageCallback
import com.test.shaadi.framework.presentation.common.BaseFragment
import com.test.shaadi.framework.presentation.common.gone
import com.test.shaadi.framework.presentation.common.setSafeOnClickListener
import com.test.shaadi.framework.presentation.common.visible
import com.test.shaadi.framework.presentation.memberlist.state.MemberListStateEvent
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_members.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class MemberFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(R.layout.fragment_members), CardStackListener {

    private val cardStackView by lazy { activity?.findViewById<CardStackView>(R.id.card_stack_view) }
    private val manager by lazy { CardStackLayoutManager(activity, this) }
    private lateinit var adapter: CardStackAdapter
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
        progress_bar.visible() // ideally should be state event based but added here temp
        setupButton()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.hasSyncBeenExecuted()
            .observe(viewLifecycleOwner, Observer { hasSyncBeenExecuted ->
                if (hasSyncBeenExecuted) {
                    // load from cache and update ui
                    progress_bar.gone()
                    loadCachedMembers()
                }
            })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.memberList.let {
                    it?.let { list ->
                        if (list.isNotEmpty()) {
                            button_container.visible()
                            setupCardStackView(list)
                        } else {
                            Toast.makeText(
                                activity,
                                "Error occurred while fetching data...",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                viewState.is_accepted.let {

                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let { message ->
                uiController.onResponseReceived(
                    response = message.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    private fun loadCachedMembers() {
        viewModel.setStateEvent(
            MemberListStateEvent.GetMembersEvent()
        )
    }

    private fun updateMembers(memberEach: MemberEach, is_accepted: Int) {
        viewModel.setStateEvent(
            MemberListStateEvent.AcceptDeclineEvent(is_accepted, memberEach)
        )
    }

    // card related functionality below

    private fun setupCardStackView(memberList: List<MemberEach>) {
        initialize(memberList)
    }

    private fun setupButton() {
        button_container.gone()
        skip_button.setSafeOnClickListener {
            disLike()
        }

        like_button.setSafeOnClickListener {
            like()
        }
    }

    private fun initialize(memberList: List<MemberEach>) {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView?.layoutManager = manager
        adapter = CardStackAdapter(memberList)
        cardStackView?.adapter = adapter
        cardStackView?.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction == Direction.Left) {
            disLike()
        } else if (direction == Direction.Right) {
            like()
        }
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }

    private fun like() {
        try {
            if (manager.topPosition >= 0 && adapter.getSpots().isNotEmpty()) {
                val memberEach = adapter.getSpots().get(manager.topPosition)
                updateMembers(memberEach, 1)
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager.setSwipeAnimationSetting(setting)
                cardStackView?.swipe()
            }
        } catch (e: Exception) {

        }

    }

    private fun disLike() {
        try {
            if (manager.topPosition >= 0 && adapter.getSpots().isNotEmpty()) {
                val memberEach = adapter.getSpots().get(manager.topPosition)
                updateMembers(memberEach, -1)
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager.setSwipeAnimationSetting(setting)
                cardStackView?.swipe()
            }
        } catch (e: Exception) {

        }
    }
}