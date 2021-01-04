package com.rinftech.marketee.presentation

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
import kotlinx.android.synthetic.main.review_selected_marketing_camapign_fragment.*
import kotlinx.android.synthetic.main.selected_marketing_campaign_perks_item.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class ReviewSelectedMarketingCampaignFragment : Fragment(), KoinComponent {
    private val viewModel: MainActivityViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.review_selected_marketing_camapign_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }


    private fun setupAdapter() {
        val reviewCampaignAdapter = ReviewCampaignRecyclerAdapter()
        reviewMarketingCampaignRecyclerView.layoutManager = LinearLayoutManager(context)
        reviewMarketingCampaignRecyclerView.adapter = reviewCampaignAdapter

        viewModel.selectedMarketingCampaignLiveData.observe(
            viewLifecycleOwner,
            Observer { marketingCampaign ->
                reviewCampaignAdapter.campaignPerksList = marketingCampaign.features
                reviewCampaignAdapter.notifyDataSetChanged()
                marketingCampaignPrice.text = marketingCampaign.price.filter { it.isDigit() }
                marketingCampaignTitle.text = marketingCampaign.campaignName
            })
    }

    class ReviewCampaignRecyclerAdapter :
        RecyclerView.Adapter<ReviewCampaignRecyclerAdapter.ViewHolder>() {
        var campaignPerksList: ArrayList<String> = ArrayList()

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.selected_marketing_campaign_perks_item, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = campaignPerksList[position]
        }

        override fun getItemCount() = campaignPerksList.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textView: TextView = view.campaignPerkTv
        }
    }

}