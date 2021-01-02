package com.rinftech.marketee.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rinftech.marketee.R
import com.rinftech.marketee.domain.MarketingCampaign
import com.rinftech.marketee.domain.Specific
import kotlinx.android.synthetic.main.select_marketing_offer.*
import kotlinx.android.synthetic.main.select_targeting_specifics_fragment.*
import kotlinx.android.synthetic.main.targeting_specific_item.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class SelectMarketingCampaignFragment : Fragment(), KoinComponent {
    private val viewModel: MainActivityViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.select_marketing_offer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        val selectMarketingCampaignAdapter = SelectMarketingCampaignRecyclerAdapter()

        viewModel.filteredMarketingCampaignListLiveData.observe(
            viewLifecycleOwner,
            Observer<List<MarketingCampaign>> {
                selectMarketingCampaignAdapter.marketingCampaignList =
                    it as ArrayList<MarketingCampaign>
            })

        selectMarketingCampaignAdapter.onItemClick = { marketingCampaign ->
            viewModel.goToReviewSelectedCampaign(marketingCampaign)
        }

        marketingOffersRecyclerView.layoutManager = LinearLayoutManager(context)
        marketingOffersRecyclerView.adapter = selectMarketingCampaignAdapter
    }

    class SelectMarketingCampaignRecyclerAdapter
        : RecyclerView.Adapter<SelectMarketingCampaignRecyclerAdapter.ViewHolder>() {

        var marketingCampaignList = ArrayList<MarketingCampaign>()

        //this is a callback
        var onItemClick: ((MarketingCampaign) -> Unit)? = null

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.targeting_specific_item, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = marketingCampaignList[position].campaignName
        }

        override fun getItemCount() = marketingCampaignList.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textView: TextView = view.targetingSpecificItemTv

            init {
                view.setOnClickListener {
                    onItemClick?.invoke(marketingCampaignList[adapterPosition])
                }
            }
        }
    }
}
