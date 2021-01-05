package com.rinftech.marketee.presentation.fragments

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
import com.rinftech.marketee.presentation.MainActivityViewModel
import kotlinx.android.synthetic.main.select_targeting_specifics_fragment.*
import kotlinx.android.synthetic.main.targeting_specific_item.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class SelectChannelFragment : Fragment(), KoinComponent {
    private val viewModel: MainActivityViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.select_channel_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        val selectChannelAdapter = SelectChannelRecyclerAdapter()
        viewModel.channelListLiveData.observe(viewLifecycleOwner, Observer<List<String>> {
            if (it.size == 1) {
                selectChannelAdapter.channelList = arrayListOf(it[0])
            } else {
                selectChannelAdapter.channelList = it as ArrayList<String>
            }
        })
        selectChannelAdapter.onItemClick = { channelName ->
            viewModel.goToSelectMarketingCampaign(channelName)
        }
        specificsRecyclerView.layoutManager = LinearLayoutManager(context)
        specificsRecyclerView.adapter = selectChannelAdapter
    }

    class SelectChannelRecyclerAdapter :
        RecyclerView.Adapter<SelectChannelRecyclerAdapter.ViewHolder>() {

        var channelList = ArrayList<String>()

        //this is a callback
        var onItemClick: ((String) -> Unit)? = null

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.targeting_specific_item, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = channelList[position]
        }

        override fun getItemCount() = channelList.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textView: TextView = view.targetingSpecificNameTv

            init {
                view.setOnClickListener {
                    onItemClick?.invoke(channelList[adapterPosition])
                }
            }
        }
    }
}