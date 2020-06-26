package com.test.shaadi.framework.presentation

import com.test.shaadi.business.domain.state.Response
import com.test.shaadi.business.domain.state.StateMessageCallback

interface UIController {
    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )
}