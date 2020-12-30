package com.rinftech.marketee.presentation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rinftech.marketee.R
import com.rinftech.marketee.domain.Specific
import kotlinx.android.synthetic.main.choose_targeting_specifics_fragment.*
import kotlinx.android.synthetic.main.targeting_specific_item.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class ChooseTargetingSpecificsFragment : Fragment(), KoinComponent {
    private val viewModel: MainActivityViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.choose_targeting_specifics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        resetFilterBtn.setOnClickListener {
            viewModel.resetFilters()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.filteredMarketingOfferListLiveData.observeForever {
            Toast.makeText(context, "Number of Offers = ${it.size}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAdapter() {
        val specificsAdapter = RecyclerAdapter(
            viewModel.specificsList,
            viewModel.specificsListForFilteringOffersLiveData
        )
        specificsAdapter.onItemClick = { specific ->
            viewModel.toggleSpecificAndFilterOffers(specific)
        }
        specificsRecyclerView.layoutManager = LinearLayoutManager(context)
        specificsRecyclerView.adapter = specificsAdapter
        specificsAdapter.notifyDataSetChanged()
    }

    class RecyclerAdapter(
        specificsListLiveData: LiveData<List<Specific>>,
        specificsListForFilteringOffersLiveData: LiveData<MutableList<Specific>>
    ) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

        private var specificsListForFilteringOffers = ArrayList<Specific>()
        private var specificsList = ArrayList<Specific>()

        //this is a callback
        var onItemClick: ((Specific) -> Unit)? = null

        init {
            specificsListLiveData.observeForever {
                specificsList = it as ArrayList<Specific>
                notifyDataSetChanged()
            }
            specificsListForFilteringOffersLiveData.observeForever {
                specificsListForFilteringOffers = it as ArrayList<Specific>
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.targeting_specific_item, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = specificsList[position].specificName
            if (specificsListForFilteringOffers.contains(specificsList[position])) {
                viewHolder.textView.setBackgroundColor(Color.parseColor("#567845"))
            } else{
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