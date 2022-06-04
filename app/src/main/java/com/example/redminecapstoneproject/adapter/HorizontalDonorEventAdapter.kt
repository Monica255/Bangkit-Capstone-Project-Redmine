package com.example.redminecapstoneproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ItemHorizontalDonationEventBinding
import com.example.redminecapstoneproject.databinding.ItemHorizontalDonorReqBinding
import com.example.redminecapstoneproject.helper.HelperBloodDonors
import com.example.redminecapstoneproject.helper.HelperUserDetail
import com.example.redminecapstoneproject.ui.testing.DonorEvent
import com.example.redminecapstoneproject.ui.testing.DonorRequest

class HorizontalDonorEventAdapter(private val list: List<DonorEvent>?):
    RecyclerView.Adapter<HorizontalDonorEventAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private var binding: ItemHorizontalDonationEventBinding,private val ctx:Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DonorEvent) {
            binding.tvEventName.text=data.name
            binding.tvEventPlace.text=data.place

            Glide.with(itemView.context)
                .load(data.image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(binding.imgEvent)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemHorizontalDonationEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val ctx=parent.context
        return ListViewHolder(binding,ctx)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if(list!==null) {
            holder.bind(list[position])
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(list[holder.adapterPosition])
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DonorEvent)
    }
    override fun getItemCount(): Int=list?.size?:0

}

