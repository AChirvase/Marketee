package com.rinftech.marketee.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rinftech.marketee.data.Repository
import com.rinftech.marketee.domain.MarketingOffer
import com.rinftech.marketee.domain.Specific
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

sealed class MainActivityViewState {
    object ChooseTargetingSpecifics : MainActivityViewState()
    object ChooseMarketingSpecifics : MainActivityViewState()
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
            it.value = MainActivityViewState.ChooseTargetingSpecifics
        }
    }

    val specificsList: MutableLiveData<List<Specific>> by lazy {
        MutableLiveData<List<Specific>>().also {
            loadSpecificsList()
        }
    }


    val allMarketingOfferList: MutableLiveData<List<MarketingOffer>> by lazy {
        MutableLiveData<List<MarketingOffer>>().also {
            loadMarketingOffersList()
        }
    }

    val filteredMarketingOfferListLiveData: MutableLiveData<List<MarketingOffer>> by lazy {
        MutableLiveData<List<MarketingOffer>>().also { allMarketingOfferList }
    }

    private val specificsListForFilteringOffers = ArrayList<Specific>()

    init {
        repository.getSpecificsListLiveData().observeForever {
            specificsList.value = it
        }
        repository.getMarketingOffersListLiveData().observeForever {
            allMarketingOfferList.value = it
        }
    }

    fun listenForViewStateChange() {

    }

    private fun loadSpecificsList() =
        GlobalScope.launch {
            repository.loadSpecificsList()
        }

    private fun loadMarketingOffersList() =
        GlobalScope.launch {
            repository.loadMarketingOffersList()
        }

    fun addSpecificAndFilterOffers(specific: Specific) {
        specificsListForFilteringOffers.add(specific)
        filterMarketingOffersByCommonChannels(getCommonChannels())
    }

    private fun filterMarketingOffersByCommonChannels(commonChannels: List<String>) {
        val filteredMarketingOfferList = ArrayList<MarketingOffer>()

        //add each marketing offer that belongs to a common channel
        allMarketingOfferList.value?.forEach { marketingOffer ->
            if (commonChannels.contains(marketingOffer.channelName)) {
                filteredMarketingOfferList.add(marketingOffer)
            }
        }
        //update the live data
        filteredMarketingOfferListLiveData.value = filteredMarketingOfferList
    }

    private fun getCommonChannels(): List<String> {
        //should not normally happen
        if (specificsListForFilteringOffers.isNullOrEmpty()) {
            Log.e(TAG, "ERROR - specificsListForFilteringOffers is empty!")
            return ArrayList()
        }

        var intersectionOfChannels = HashSet<String>()
        //initialize with first item
        intersectionOfChannels.addAll(specificsListForFilteringOffers[0].channelsNamesList)
        //intersect with all the other specifics channels
        specificsListForFilteringOffers.forEach {
            intersectionOfChannels =
                intersectionOfChannels.intersect(it.channelsNamesList).toHashSet()
        }

        return intersectionOfChannels.toList()
    }

    fun resetFilters() {
        specificsListForFilteringOffers.clear()
        filteredMarketingOfferListLiveData.value = ArrayList()
    }

}