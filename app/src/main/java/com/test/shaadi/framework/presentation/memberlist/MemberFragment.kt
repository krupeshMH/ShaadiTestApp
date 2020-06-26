package com.test.shaadi.framework.presentation.memberlist

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.test.shaadi.R
import com.test.shaadi.business.domain.model.MemberEach
import com.test.shaadi.framework.presentation.common.BaseFragment
import com.test.shaadi.framework.presentation.memberlist.state.MemberListStateEvent
import com.yuyakaido.android.cardstackview.*
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
) : BaseFragment(R.layout.fragment_members), CardStackListener {

    private val cardStackView by lazy { activity?.findViewById<CardStackView>(R.id.card_stack_view) }
    private val manager by lazy { CardStackLayoutManager(activity, this) }
    //private val adapter by lazy { CardStackAdapter(createSpots()) }

    private val viewModel: MemberViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
        setupButton()
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeRefresh()
        subscribeObservers()
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
                    it?.let { list ->
                        setupCardStackView(list)
                    }
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

    private fun setupCardStackView(memberList: List<MemberEach>) {
        initialize(memberList)
    }

    private fun setupButton() {
        val skip = activity?.findViewById<View>(R.id.skip_button)
        skip?.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView?.swipe()
        }

        //val rewind = activity?.findViewById<View>(com.yuyakaido.android.cardstackview.R.id.rewind_button)
        /*rewind_button.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            cardStackView?.rewind()
        }*/

        val like = activity?.findViewById<View>(R.id.like_button)
        like?.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView?.swipe()
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
        cardStackView?.adapter = CardStackAdapter(memberList)
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
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }
}