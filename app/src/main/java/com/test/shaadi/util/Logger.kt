package com.test.shaadi.util

import android.util.Log
import com.test.shaadi.util.Constants.DEBUG
import com.test.shaadi.util.Constants.TAG

var isUnitTest = false

fun printLogD(className: String?, message: String) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    } else if (DEBUG && isUnitTest) {
        println("$className: $message")
    }
}
