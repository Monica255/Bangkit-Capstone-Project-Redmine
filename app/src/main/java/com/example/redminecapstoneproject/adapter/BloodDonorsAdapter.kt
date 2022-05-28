package com.example.redminecapstoneproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ItemVerticalAlertDialogBinding
import com.example.redminecapstoneproject.databinding.ItemVerticalBloodDonorsBinding
import com.example.redminecapstoneproject.helper.helperBloodDonors
import com.example.redminecapstoneproject.ui.testing.BloodDonors

class BloodDonorsAdapter(private val list: List<BloodDonors>?):
    RecyclerView.Adapter<BloodDonorsAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private var binding: ItemVerticalBloodDonorsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BloodDonors) {
            binding.tvDonorName.text=data.name
            binding.tvDonorLocation.text=helperBloodDonors.toLocation(data.city,data.province)
            binding.tvBloodType.text= helperBloodDonors.toBloodType(data.bloodType,data.rhesus)


            val x: Int =if(data.gender=="male") R.drawable.img_profile_placeholder_male else R.drawable.img_profile_placeholder_female
            Glide.with(itemView.context)
                .load(x)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(binding.imgProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemVerticalBloodDonorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
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
        fun onItemClicked(data: BloodDonors)
    }
    override fun getItemCount(): Int=list?.size?:0

}

