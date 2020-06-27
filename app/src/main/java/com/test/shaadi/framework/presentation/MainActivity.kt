package com.test.shaadi.framework.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.test.shaadi.R
import com.test.shaadi.business.domain.state.Response
import com.test.shaadi.business.domain.state.StateMessageCallback
import com.test.shaadi.business.domain.state.UIComponentType
import com.test.shaadi.framework.presentation.common.AppFragmentFactory
import com.test.shaadi.framework.presentation.common.displayToast
import com.test.shaadi.framework.presentation.common.gone
import com.test.shaadi.framework.presentation.common.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : AppCompatActivity(), UIController {
    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun setFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun inject() {
        (application as BaseApplication).appComponent
            .inject(this)
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        if (isDisplayed)
            main_progress_bar.visible()
        else
            main_progress_bar.gone()
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {

        when (response.uiComponentType) {

            is UIComponentType.Toast -> {
                response.message?.let {
                    displayToast(
                        message = it,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is UIComponentType.None -> {
                Log.i("TAG", "onResponseReceived: ${response.message}")
                stateMessageCallback.removeMessageFromStack()
            }
        }
    }
}