package com.rinftech.marketee.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rinftech.marketee.R
import com.rinftech.marketee.data.Repository
import com.rinftech.marketee.domain.MarketingCampaign
import com.rinftech.marketee.domain.Specific
import com.rinftech.marketee.utils.Constants.Companion.TO_EMAIL_ADDRESS
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent


sealed class MainActivityViewState {
    object SelectTargetingSpecifics : MainActivityViewState()
    object TargetingSpecificsSelected : MainActivityViewState()
    object SelectChannel : MainActivityViewState()
    object SelectMarketingCampaign : MainActivityViewState()
    object ReviewSelectedMarketingCampaign : MainActivityViewState()
    object ExitApp : MainActivityViewState()
}

class MainActivityViewModel(
    private val context: Context,
    private val repository: Repository
) : ViewModel(), KoinComponent {

    companion object {
        private const val TAG = "MainActivityViewModel"
    }

    val viewState: MutableLiveData<MainActivityViewState> by lazy {
        MutableLiveData<MainActivityViewState>().also {
            it.value = MainActivityViewState.SelectTargetingSpecifics
        }
    }

    val specificsListLiveData: MutableLiveData<List<Specific>> by lazy {
        MutableLiveData<List<Specific>>().also {
            loadSpecificsList()
        }
    }

    val allMarketingCampaignList: MutableLiveData<List<MarketingCampaign>> by lazy {
        MutableLiveData<List<MarketingCampaign>>().also {
            loadMarketingCampaignsList()
        }
    }

    val channelListLiveData: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    val filteredMarketingCampaignListLiveData: MutableLiveData<List<MarketingCampaign>> by lazy {
        MutableLiveData<List<MarketingCampaign>>().also { allMarketingCampaignList }
    }
    val specificsListForFilteringCampaignsLiveData: MutableLiveData<MutableList<Specific>> =
        MutableLiveData()
    private val specificsListForFilteringCampaigns = ArrayList<Specific>()
    val selectedMarketingCampaignLiveData: MutableLiveData<MarketingCampaign> by lazy {
        MutableLiveData<MarketingCampaign>()
    }

    init {
        repository.getSpecificsListLiveData().observeForever {
            specificsListLiveData.value = it
        }
        repository.getMarketingCampaignListLiveData().observeForever {
            allMarketingCampaignList.value = it
        }
    }

    private fun loadSpecificsList() =
        GlobalScope.launch {
            repository.loadSpecificsList()
        }

    private fun loadMarketingCampaignsList() =
        GlobalScope.launch {
            repository.loadMarketingCampaignsList()
        }

    fun toggleSpecificAndFilterCampaigns(specific: Specific) {
        if (specificsListForFilteringCampaigns.contains(specific)) {
            specificsListForFilteringCampaigns.remove(specific)
        } else {
            specificsListForFilteringCampaigns.add(specific)
        }
        //update the corresponding live data to notify activity
        specificsListForFilteringCampaignsLiveData.value = specificsListForFilteringCampaigns

        //update the viewState so Activity can change accordingly
        if (!specificsListForFilteringCampaigns.isNullOrEmpty()) {
            if (viewState.value != MainActivityViewState.TargetingSpecificsSelected) {
                viewState.value = MainActivityViewState.TargetingSpecificsSelected
            }
        } else {
            viewState.value = MainActivityViewState.SelectTargetingSpecifics
        }

        filterMarketingCampaignsByCommonChannelList(getCommonChannels())
    }

    fun goToSelectMarketingCampaign(channelName: String) {
        viewState.value = MainActivityViewState.SelectMarketingCampaign
        filterMarketingCampaignsByCommonChannel(channelName)
    }

    private fun filterMarketingCampaignsByCommonChannelList(commonChannels: List<String>) {
        filteredMarketingCampaignListLiveData.value =
            allMarketingCampaignList.value?.filter { commonChannels.contains(it.channelName) }
    }

    private fun filterMarketingCampaignsByCommonChannel(channelName: String) {
        filteredMarketingCampaignListLiveData.value =
            allMarketingCampaignList.value?.filter { channelName == it.channelName }
    }

    private fun getCommonChannels(): List<String> {
        //normally should not happen
        if (specificsListForFilteringCampaigns.isNullOrEmpty()) {
            Log.e(TAG, "ERROR - specificsListForFilteringCampaigns is empty!")
            return ArrayList()
        }

        var intersectionOfChannels = HashSet<String>()
        //initialize with first item
        intersectionOfChannels.addAll(specificsListForFilteringCampaigns[0].channelsNamesList)
        //intersect with all the other specifics channels
        specificsListForFilteringCampaigns.forEach {
            intersectionOfChannels =
                intersectionOfChannels.intersect(it.channelsNamesList).toHashSet()
        }

        channelListLiveData.value = intersectionOfChannels.toList()

        return intersectionOfChannels.toList()
    }

    private fun resetFilters() {
        specificsListForFilteringCampaigns.clear()
        specificsListForFilteringCampaignsLiveData.value = specificsListForFilteringCampaigns
        filteredMarketingCampaignListLiveData.value = ArrayList()
    }

    private fun goToSelectChannel() {
        viewState.value = MainActivityViewState.SelectChannel
    }

    fun onMainActivityFabPressed() {
        when (viewState.value) {
            is MainActivityViewState.TargetingSpecificsSelected -> goToSelectChannel()
            is MainActivityViewState.ReviewSelectedMarketingCampaign -> buyMarketingCampaign()
            else -> {
            }
        }
    }

    private fun buyMarketingCampaign() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(TO_EMAIL_ADDRESS))
            putExtra(Intent.EXTRA_SUBJECT, getEmailSubject())
            putExtra(Intent.EXTRA_TEXT, getEmailText())
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    private fun getEmailText(): String =
        context.resources.getString(
            R.string.email_text,
            selectedMarketingCampaignLiveData.value?.campaignName,
            selectedMarketingCampaignLiveData.value?.channelName,
            selectedMarketingCampaignLiveData.value?.price,
            selectedMarketingCampaignLiveData.value?.features?.joinToString(
                separator = "\n- ",
                prefix = "- "
            )
        )

    private fun getEmailSubject(): String =
        context.resources.getString(
            R.string.email_subject,
            selectedMarketingCampaignLiveData.value?.campaignName
        )

    fun goToReviewSelectedCampaign(marketingCampaign: MarketingCampaign) {
        selectedMarketingCampaignLiveData.value = marketingCampaign
        viewState.value = MainActivityViewState.ReviewSelectedMarketingCampaign
    }

    fun onReviewMarketingCampaignCollapsed() {
        viewState.value = MainActivityViewState.SelectMarketingCampaign
    }

    fun onBackPressed() {
        when (viewState.value) {
            is MainActivityViewState.SelectTargetingSpecifics -> {
                viewState.value = MainActivityViewState.ExitApp
            }
            is MainActivityViewState.TargetingSpecificsSelected -> {
                viewState.value = MainActivityViewState.SelectTargetingSpecifics
                resetFilters()
            }
            is MainActivityViewState.SelectChannel -> {
                resetFilters()
                viewState.value = MainActivityViewState.SelectTargetingSpecifics
            }
            is MainActivityViewState.SelectMarketingCampaign -> {
                viewState.value = MainActivityViewState.SelectChannel
            }
            is MainActivityViewState.ReviewSelectedMarketingCampaign -> {
                viewState.value = MainActivityViewState.SelectMarketingCampaign
            }
            else -> {
                viewState.value = MainActivityViewState.SelectTargetingSpecifics
            }
        }
    }

}