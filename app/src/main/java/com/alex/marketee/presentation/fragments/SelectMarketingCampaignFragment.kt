package com.alex.marketee.presentation.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.marketee.R
import com.alex.marketee.domain.MarketingCampaign
import com.alex.marketee.presentation.MainActivityViewModel
import com.alex.marketee.presentation.MainActivityViewState
import kotlinx.android.synthetic.main.marketing_campaign_item.view.*
import kotlinx.android.synthetic.main.select_marketing_offer.*
import me.saket.inboxrecyclerview.animation.ItemExpandAnimator
import me.saket.inboxrecyclerview.dimming.DimPainter
import me.saket.inboxrecyclerview.page.PullToCollapseListener
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
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            if (it == MainActivityViewState.SelectMarketingCampaign) {
                marketingCampaignRecyclerView.collapse()
            }
        })
        setupExpandablePageLayout()
        setupRecyclerView(setupAdapter())
    }

    private fun setupExpandableFragment() {
        var expandableFragment =
            childFragmentManager.findFragmentById(marketingCampaignExpandablePage.id) as ReviewSelectedMarketingCampaignFragment?
        if (expandableFragment == null) {
            expandableFragment = ReviewSelectedMarketingCampaignFragment()
        }

        childFragmentManager
            .beginTransaction()
            .replace(marketingCampaignExpandablePage.id, expandableFragment)
            .commitNowAllowingStateLoss()
    }

    private fun setupAdapter(): SelectMarketingCampaignRecyclerAdapter {
        val selectMarketingCampaignAdapter =
            SelectMarketingCampaignRecyclerAdapter(requireContext())

        viewModel.filteredMarketingCampaignListLiveData.observe(
            viewLifecycleOwner,
            Observer<List<MarketingCampaign>> {
                selectMarketingCampaignAdapter.marketingCampaignList =
                    it as ArrayList<MarketingCampaign>
                selectMarketingCampaignAdapter.notifyDataSetChanged()
            })

        selectMarketingCampaignAdapter.onItemClick = {
            viewModel.goToReviewSelectedCampaign(it.first)
            marketingCampaignRecyclerView.expandItem(it.second)
            setupExpandableFragment()
        }

        return selectMarketingCampaignAdapter
    }

    private fun setupRecyclerView(adapter: SelectMarketingCampaignRecyclerAdapter) {
        marketingCampaignRecyclerView.layoutManager = LinearLayoutManager(context)
        marketingCampaignRecyclerView.expandablePage = marketingCampaignExpandablePage
        marketingCampaignRecyclerView.dimPainter = DimPainter.listAndPage(
            listColor = Color.WHITE,
            listAlpha = 0.75F,
            pageColor = Color.WHITE,
            pageAlpha = 0.65f
        )
        marketingCampaignRecyclerView.itemExpandAnimator = ItemExpandAnimator.split()

        marketingCampaignRecyclerView.adapter = adapter
    }

    private fun setupExpandablePageLayout() {
        marketingCampaignExpandablePage.pullToCollapseThresholdDistance =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics)
                .toInt()
        marketingCampaignExpandablePage.pullToCollapseListener.addOnPullListener(object :
            PullToCollapseListener.OnPullListener {
            override fun onPull(
                deltaY: Float,
                currentTranslationY: Float,
                upwardPull: Boolean,
                deltaUpwardPull: Boolean,
                collapseEligible: Boolean
            ) {
                if (collapseEligible) {
                    viewModel.onReviewMarketingCampaignCollapsed()
                }
            }

            override fun onRelease(collapseEligible: Boolean) {
                //NA
            }

        })
    }

    class SelectMarketingCampaignRecyclerAdapter(val context: Context) :
        RecyclerView.Adapter<SelectMarketingCampaignRecyclerAdapter.ViewHolder>() {

        var marketingCampaignList = ArrayList<MarketingCampaign>()

        //this is a callback
        var onItemClick: ((Pair<MarketingCampaign, Long>) -> Unit)? = null

        init {
            setHasStableIds(true)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.marketing_campaign_item, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.marketingCampaignName.text = marketingCampaignList[position].campaignName
            viewHolder.channelName.text = marketingCampaignList[position].channelName
            viewHolder.price.text = marketingCampaignList[position].price
            viewHolder.numberOfFeatures.text = context.getString(
                R.string.number_of_features,
                marketingCampaignList[position].features.size
            )
        }

        override fun getItemCount() = marketingCampaignList.size

        override fun getItemId(position: Int): Long =
            marketingCampaignList[position].hashCode().toLong()

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var marketingCampaignName: TextView = view.marketingCampaignNameTv
            var channelName: TextView = view.channelNameTv
            var price: TextView = view.priceTv
            var numberOfFeatures: TextView = view.numberOfFeaturesTv

            init {
                view.setOnClickListener {
                    onItemClick?.invoke(
                        Pair(
                            marketingCampaignList[adapterPosition],
                            getItemId(adapterPosition)
                        )
                    )
                }
            }
        }
    }
}
