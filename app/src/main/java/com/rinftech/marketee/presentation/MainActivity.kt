package com.rinftech.marketee.presentation

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rinftech.marketee.R
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {
    private val viewModel: MainActivityViewModel by viewModel()
    private val chooseTargetingSpecificsFragment = ChooseTargetingSpecificsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        viewModel.listenForViewStateChange()
    }

    override fun onResume() {
        super.onResume()
        subscribeForViewStateChange()
    }

    private fun subscribeForViewStateChange() {
        viewModel.viewState.observe(this, Observer { viewState -> updateViewState(viewState) })
    }

    private fun updateViewState(viewState: MainActivityViewState) {
        supportFragmentManager.executePendingTransactions()
        when (viewState) {
            is MainActivityViewState.ChooseTargetingSpecifics -> setChooseTargetingSpecificsFragment()
            else -> {
                setChooseTargetingSpecificsFragment()
            }
        }
    }

    private fun setChooseTargetingSpecificsFragment() {
        if (chooseTargetingSpecificsFragment.isVisible) return
        supportFragmentManager.beginTransaction().replace(
            R.id.mainActivityFragmentContainer,
            chooseTargetingSpecificsFragment
        ).commit()
    }

}