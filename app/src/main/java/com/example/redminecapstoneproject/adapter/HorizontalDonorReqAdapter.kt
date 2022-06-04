package com.example.redminecapstoneproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redminecapstoneproject.databinding.ItemHorizontalDonorReqBinding
import com.example.redminecapstoneproject.helper.HelperBloodDonors
import com.example.redminecapstoneproject.helper.HelperUserDetail
import com.example.redminecapstoneproject.ui.testing.DonorRequest

class HorizontalDonorReqAdapter(private val list: List<DonorRequest>?):
    RecyclerView.Adapter<HorizontalDonorReqAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private var binding: ItemHorizontalDonorReqBinding,private val ctx:Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DonorRequest) {
            binding.tvDonorReqName.text=data.contactName
            binding.tvDonorReqCity.text=HelperBloodDonors.toLocation(data.city?.lowercase()?.replaceFirstChar(Char::titlecase),
                data.province?.let { HelperUserDetail.getProvinceName(it).lowercase()
                    .replaceFirstChar(Char::titlecase) })
            binding.tvBloodType.text= HelperBloodDonors.toBloodType(data.bloodType,data.rhesus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemHorizontalDonorReqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun onItemClicked(data: DonorRequest)
    }
    override fun getItemCount(): Int=list?.size?:0

}

