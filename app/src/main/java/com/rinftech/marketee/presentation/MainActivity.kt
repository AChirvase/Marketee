package com.rinftech.marketee.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rinftech.marketee.R
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {
    private val viewModel: MainActivityViewModel by viewModel()

    private val selectTargetingSpecificsFragment = SelectTargetingSpecificsFragment()
    private val selectChannelFragment = SelectChannelFragment()
    private val selectMarketingCampaignFragment = SelectMarketingCampaignFragment()
    private val reviewSelectedMarketingCampaignFragment = ReviewSelectedMarketingCampaignFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
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
            is MainActivityViewState.SelectTargetingSpecifics -> setSelectTargetingSpecificsFragment()
            is MainActivityViewState.SelectChannel -> setSelectChannelFragment()
            is MainActivityViewState.SelectMarketingCampaign -> setSelectMarketingCampaignFragment()
            is MainActivityViewState.ReviewSelectedMarketingCampaign -> setReviewSelectedMarketingCampaignFragment()
        }
    }

    private fun setReviewSelectedMarketingCampaignFragment() {
        if (reviewSelectedMarketingCampaignFragment.isVisible) return
        supportFragmentManager.beginTransaction().replace(
            R.id.mainActivityFragmentContainer,
            reviewSelectedMarketingCampaignFragment
        ).commit()
    }

    private fun setSelectMarketingCampaignFragment() {
        if (selectMarketingCampaignFragment.isVisible) return
        supportFragmentManager.beginTransaction().replace(
            R.id.mainActivityFragmentContainer,
            selectMarketingCampaignFragment
        ).commit()
    }

    private fun setSelectChannelFragment() {
        if (selectChannelFragment.isVisible) return
        supportFragmentManager.beginTransaction().replace(
            R.id.mainActivityFragmentContainer,
            selectChannelFragment
        ).commit()
    }

    private fun setSelectTargetingSpecificsFragment() {
        if (selectTargetingSpecificsFragment.isVisible) return
        supportFragmentManager.beginTransaction().replace(
            R.id.mainActivityFragmentContainer,
            selectTargetingSpecificsFragment
        ).commit()
    }

}