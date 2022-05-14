package com.example.redminecapstoneproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redminecapstoneproject.databinding.ItemVerticalAlertDialogBinding

class AlertDialogAdapter(private val list: List<String>?):
    RecyclerView.Adapter<AlertDialogAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private var binding: ItemVerticalAlertDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.tvText.text=data
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
        fun onItemClicked(data: String)
    }
    override fun getItemCount(): Int=list?.size?:0
}

