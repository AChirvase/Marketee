package com.rinftech.marketee.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rinftech.marketee.data.Repository
import com.rinftech.marketee.domain.MarketingCampaign
import com.rinftech.marketee.domain.Specific
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

sealed class MainActivityViewState {
    object SelectTargetingSpecifics : MainActivityViewState()
    object SelectChannel : MainActivityViewState()
    object SelectMarketingCampaign : MainActivityViewState()
    object ReviewSelectedMarketingCampaign : MainActivityViewState()
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
    private lateinit var selectedMarketingCampaign: MarketingCampaign

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

    fun resetFilters() {
        specificsListForFilteringCampaigns.clear()
        specificsListForFilteringCampaignsLiveData.value = specificsListForFilteringCampaigns
        filteredMarketingCampaignListLiveData.value = ArrayList()
    }

    fun goToSelectChannel() {
        viewState.value = MainActivityViewState.SelectChannel
    }

    fun goToReviewSelectedCampaign(marketingCampaign: MarketingCampaign) {
        viewState.value = MainActivityViewState.ReviewSelectedMarketingCampaign
        selectedMarketingCampaign = marketingCampaign
    }


}