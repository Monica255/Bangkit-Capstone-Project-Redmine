package com.example.redminecapstoneproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ItemVerticalDonorReqBinding
import com.example.redminecapstoneproject.helper.helperBloodDonors
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.helper.helperUserDetail
import com.example.redminecapstoneproject.ui.testing.DonorRequest
import com.google.firebase.auth.FirebaseAuth

class VerticalDonorReqAdapter(private val list: List<DonorRequest>?,val mBloodType:String):
    RecyclerView.Adapter<VerticalDonorReqAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private var binding: ItemVerticalDonorReqBinding,private val ctx:Context,private val mBloodType: String) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DonorRequest) {
            binding.tvDonorReqName.text=data.contactName
            binding.tvDonorReqCity.text=helperBloodDonors.toLocation(data.city?.lowercase()?.replaceFirstChar(Char::titlecase),
                data.province?.let { helperUserDetail.getProvinceName(it).lowercase()
                    .replaceFirstChar(Char::titlecase) })
            val bt=helperBloodDonors.toBloodType(data.bloodType,data.rhesus)
            binding.tvBloodType.text= bt
            binding.tvDonorReqDes.text=data.description
            binding.tvPostTime.text= data.time?.let { helperDate.toPostTime(it,ctx) }

            if(data.uid==FirebaseAuth.getInstance().currentUser?.uid){
                /*binding.tvCompatibility.text="Your own donor request"
                binding.tvCompatibility.setTextColor(ctx.resources.getColor(R.color.not_so_black))*/
                binding.tvCompatibility.visibility= View.GONE
            }else{
                val txt=helperBloodDonors.checkCompatibility(mBloodType,bt,ctx)
                binding.tvCompatibility.text=txt
                binding.tvCompatibility.setTextColor(helperBloodDonors.setColor(txt,ctx))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemVerticalDonorReqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val ctx=parent.context
        return ListViewHolder(binding,ctx,mBloodType)
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

