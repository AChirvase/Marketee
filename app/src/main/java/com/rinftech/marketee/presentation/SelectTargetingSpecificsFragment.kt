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
import com.rinftech.marketee.domain.Specific
import kotlinx.android.synthetic.main.select_targeting_specifics_fragment.*
import kotlinx.android.synthetic.main.targeting_specific_item.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class SelectTargetingSpecificsFragment : Fragment(), KoinComponent {
    private val viewModel: MainActivityViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.select_targeting_specifics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        goToSelectChannel.setOnClickListener {
            viewModel.goToSelectChannel()
        }
    }

    private fun setupAdapter() {
        val specificsAdapter = ChooseSpecificsRecyclerAdapter()

        viewModel.specificsListLiveData.observe(viewLifecycleOwner,
            Observer<List<Specific>> {
                specificsAdapter.specificsList = it as ArrayList<Specific>
                specificsAdapter.notifyDataSetChanged()
            })
        viewModel.specificsListForFilteringCampaignsLiveData.observe(viewLifecycleOwner,
            Observer<List<Specific>> {
                specificsAdapter.specificsListForFilteringCampaigns = it as ArrayList<Specific>
                specificsAdapter.notifyDataSetChanged()
            })

        specificsAdapter.onItemClick = { specific ->
            viewModel.toggleSpecificAndFilterCampaigns(specific)
        }
        specificsRecyclerView.layoutManager = LinearLayoutManager(context)
        specificsRecyclerView.adapter = specificsAdapter

    }

    class ChooseSpecificsRecyclerAdapter
        : RecyclerView.Adapter<ChooseSpecificsRecyclerAdapter.ViewHolder>() {

        var specificsListForFilteringCampaigns = ArrayList<Specific>()
        var specificsList = ArrayList<Specific>()

        //this is a callback
        var onItemClick: ((Specific) -> Unit)? = null

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.targeting_specific_item, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = specificsList[position].specificName
            if (specificsListForFilteringCampaigns.contains(specificsList[position])) {
                viewHolder.textView.setBackgroundColor(Color.parseColor("#567845"))
            } else {
                viewHolder.textView.setBackgroundColor(Color.parseColor("#992031"))
            }
        }

        override fun getItemCount() = specificsList.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textView: TextView = view.targetingSpecificItemTv

            init {
                view.setOnClickListener {
                    onItemClick?.invoke(specificsList[adapterPosition])
                }
            }
        }
    }
}