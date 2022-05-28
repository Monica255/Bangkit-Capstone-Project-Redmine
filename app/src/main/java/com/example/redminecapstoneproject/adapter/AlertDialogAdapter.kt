package com.example.redminecapstoneproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redminecapstoneproject.databinding.ItemVerticalAlertDialogBinding
import com.example.redminecapstoneproject.ui.testing.City
import com.example.redminecapstoneproject.ui.testing.Province

class AlertDialogAdapter(private val list: List<Any>?):
    RecyclerView.Adapter<AlertDialogAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private var binding: ItemVerticalAlertDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Any) {
            if(data is Province){
                binding.tvText.text=data.provName.replaceFirstChar(Char::titlecase)

            }else if(data is City){
                binding.tvText.text=data.cityName.replaceFirstChar(Char::titlecase)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemVerticalAlertDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun onItemClicked(data: Any)
    }
    override fun getItemCount(): Int=list?.size?:0
}

