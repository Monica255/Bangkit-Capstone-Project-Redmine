package com.example.redminecapstoneproject.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ItemHorizontalDonorReqBinding
import com.example.redminecapstoneproject.databinding.ItemVerticalAlertDialogBinding
import com.example.redminecapstoneproject.databinding.ItemVerticalBloodDonorsBinding
import com.example.redminecapstoneproject.databinding.ItemVerticalDonorReqBinding
import com.example.redminecapstoneproject.helper.helperBloodDonors
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.helper.helperUserDetail
import com.example.redminecapstoneproject.ui.testing.BloodDonors
import com.example.redminecapstoneproject.ui.testing.DonorRequest

class VerticalDonorReqAdapter(private val list: List<DonorRequest>?):
    RecyclerView.Adapter<VerticalDonorReqAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private var binding: ItemVerticalDonorReqBinding,private val ctx:Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DonorRequest) {
            binding.tvDonorReqName.text=data.contactName
            binding.tvDonorReqCity.text=helperBloodDonors.toLocation(data.city?.lowercase()?.replaceFirstChar(Char::titlecase),
                data.province?.let { helperUserDetail.getProvinceName(it)?.lowercase()?.replaceFirstChar(Char::titlecase) })
            binding.tvBloodType.text= helperBloodDonors.toBloodType(data.bloodType,data.rhesus)
            binding.tvDonorReqDes.text=data.description
            binding.tvPostTime.text= data.time?.let { helperDate.toPostTime(it,ctx) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemVerticalDonorReqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        var ctx=parent.context
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
        fun onItemClicked(data: DonorRequest)
    }
    override fun getItemCount(): Int=list?.size?:0

}

