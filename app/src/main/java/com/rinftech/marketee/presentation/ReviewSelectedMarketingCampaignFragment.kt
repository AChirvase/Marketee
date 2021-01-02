package com.rinftech.marketee.presentation

import androidx.fragment.app.Fragment
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class ReviewSelectedMarketingCampaignFragment: Fragment(), KoinComponent {
    private val viewModel: MainActivityViewModel by sharedViewModel()

}