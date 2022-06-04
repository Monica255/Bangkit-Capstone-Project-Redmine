package com.example.redminecapstoneproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redminecapstoneproject.databinding.ItemVerticalDonorReqBinding
import com.example.redminecapstoneproject.helper.HelperBloodDonors
import com.example.redminecapstoneproject.helper.HelperDate
import com.example.redminecapstoneproject.helper.HelperUserDetail
import com.example.redminecapstoneproject.ui.testing.DonorRequest
import com.google.firebase.auth.FirebaseAuth

class VerticalDonorReqAdapter(private val list: List<DonorRequest>?, private val mBloodType:String):
    RecyclerView.Adapter<VerticalDonorReqAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private var binding: ItemVerticalDonorReqBinding,private val ctx:Context,private val mBloodType: String) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DonorRequest) {
            binding.tvDonorReqName.text=data.contactName
            binding.tvDonorReqCity.text=HelperBloodDonors.toLocation(data.city?.lowercase()?.replaceFirstChar(Char::titlecase),
                data.province?.let { HelperUserDetail.getProvinceName(it).lowercase()
                    .replaceFirstChar(Char::titlecase) })
            val bt=HelperBloodDonors.toBloodType(data.bloodType,data.rhesus)
            binding.tvBloodType.text= bt
            binding.tvDonorReqDes.text=data.description
            binding.tvPostTime.text= data.time?.let { HelperDate.toPostTime(it,ctx) }

            if(data.uid==FirebaseAuth.getInstance().currentUser?.uid){
                /*binding.tvCompatibility.text="Your own donor request"
                binding.tvCompatibility.setTextColor(ctx.resources.getColor(R.color.not_so_black))*/
                binding.tvCompatibility.visibility= View.GONE
            }else{
                val txt=HelperBloodDonors.checkCompatibility(mBloodType,bt,ctx)
                binding.tvCompatibility.text=txt
                binding.tvCompatibility.setTextColor(HelperBloodDonors.setColor(txt,ctx))
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

