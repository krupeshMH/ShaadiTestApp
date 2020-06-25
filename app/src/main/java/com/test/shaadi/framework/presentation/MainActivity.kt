package com.test.shaadi.framework.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.shaadi.R
import com.test.shaadi.framework.presentation.common.AppFragmentFactory
import com.test.shaadi.framework.presentation.memberlist.MemberFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
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
}