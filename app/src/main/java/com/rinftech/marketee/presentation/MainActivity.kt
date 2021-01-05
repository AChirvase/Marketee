package com.rinftech.marketee.presentation

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.rinftech.marketee.R
import com.rinftech.marketee.presentation.fragments.SelectChannelFragment
import com.rinftech.marketee.presentation.fragments.SelectMarketingCampaignFragment
import com.rinftech.marketee.presentation.fragments.SelectTargetingSpecificsFragment
import kotlinx.android.synthetic.main.main_activity_layout.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {
    private val viewModel: MainActivityViewModel by viewModel()

    private val selectTargetingSpecificsFragment = SelectTargetingSpecificsFragment()
    private val selectChannelFragment = SelectChannelFragment()
    private val selectMarketingCampaignFragment = SelectMarketingCampaignFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed()
            }
        })
        setContentView(R.layout.main_activity_layout)
    }

    override fun onResume() {
        super.onResume()
        subscribeForViewStateChange()
        mainActivityFab.setOnClickListener {
            viewModel.onMainActivityFabPressed()
        }
    }

    private fun subscribeForViewStateChange() {
        viewModel.viewState.observe(this, Observer { viewState -> updateViewState(viewState) })
    }

    private fun updateViewState(viewState: MainActivityViewState) {
        supportFragmentManager.executePendingTransactions()
        when (viewState) {
            is MainActivityViewState.SelectTargetingSpecifics -> {
                setSelectTargetingSpecificsFragment()
                updateFab(R.drawable.ic_m_letter_white_24dp)
                mainActivityTitle.text = getString(R.string.select_targeting_specifics_title)
                mainActivitySubtitle.text = getString(R.string.select_targeting_specifics_subtitle)
            }
            is MainActivityViewState.TargetingSpecificsSelected -> {
                updateFab(R.drawable.ic_baseline_arrow_forward_24)
                mainActivityTitle.text = getString(R.string.select_targeting_specifics_title)
                mainActivitySubtitle.text = getString(R.string.select_targeting_specifics_subtitle)
            }
            is MainActivityViewState.SelectChannel -> {
                setSelectChannelFragment()
                updateFab(R.drawable.ic_baseline_arrow_back_24)
                mainActivityTitle.text = getString(R.string.select_channel_title)
                mainActivitySubtitle.text = getString(R.string.select_channel_subtitle)
            }
            is MainActivityViewState.SelectMarketingCampaign -> {
                setSelectMarketingCampaignFragment()
                updateFab(R.drawable.ic_m_letter_white_24dp)
                mainActivityTitle.text = getString(R.string.select_marketing_campaign_title)
                mainActivitySubtitle.text = getString(R.string.select_marketing_campaign_subtitle)
            }
            is MainActivityViewState.ReviewSelectedMarketingCampaign -> {
                updateFab(R.drawable.ic_baseline_add_shopping_cart_24)
            }
            is MainActivityViewState.ExitApp -> finish()
        }
    }

    private fun updateFab(iconResourceId: Int) {
        val animatedVectorDrawable = { iconRes: Int ->
            ContextCompat.getDrawable(applicationContext, iconRes) as AnimatedVectorDrawable
        }
        val icon = animatedVectorDrawable(iconResourceId)
        mainActivityFab.setImageDrawable(icon)
        icon.start()
    }

    private fun setSelectMarketingCampaignFragment() {
        if (selectMarketingCampaignFragment.isAdded) return
        supportFragmentManager.beginTransaction()
            .add(
                R.id.mainActivityFragmentContainer,
                selectMarketingCampaignFragment
            )
            .addToBackStack(null)
            .commit()
    }

    private fun setSelectChannelFragment() {
        if (selectChannelFragment.isAdded) {
            supportFragmentManager.popBackStack(selectChannelFragment.javaClass.name, 0)
            return
        }
        supportFragmentManager.beginTransaction()
            .add(
                R.id.mainActivityFragmentContainer,
                selectChannelFragment
            )
            .addToBackStack(selectChannelFragment.javaClass.name)
            .commit()
    }

    private fun setSelectTargetingSpecificsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.mainActivityFragmentContainer,
                selectTargetingSpecificsFragment
            ).addToBackStack(null)
            .commit()
    }

}