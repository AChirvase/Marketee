package com.rinftech.marketee.presentation

import android.content.Context
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
        val specificsAdapter = RecyclerAdapter(viewModel.specificsList)
        specificsAdapter.onItemClick = { specific ->
            viewModel.addSpecificAndFilterOffers(specific)
        }
        specificsRecyclerView.layoutManager = LinearLayoutManager(context)
        specificsRecyclerView.adapter = specificsAdapter
        specificsAdapter.notifyDataSetChanged()
    }

    class RecyclerAdapter(
        specificsList: LiveData<List<Specific>>,
        private var dataSet: ArrayList<Specific> = arrayListOf()
    ) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

        //this is a callback
        var onItemClick: ((Specific) -> Unit)? = null

        init {
            specificsList.observeForever {
                dataSet = it as ArrayList<Specific>
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textView: TextView = view.targetingSpecificItemTv

            init {
                view.setOnClickListener {
                    onItemClick?.invoke(dataSet[adapterPosition])
                }
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.targeting_specific_item, viewGroup, false)

            view.setOnClickListener {


            }

            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = dataSet[position].specificName
        }

        override fun getItemCount() = dataSet.size

    }
}