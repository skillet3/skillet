package com.test.skilllet.serviceprovider.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.databinding.RowTagsBinding

class TagsAdapter(val list:ArrayList<String>):RecyclerView.Adapter<TagsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsAdapter.ViewHolder {
       val view=RowTagsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagsAdapter.ViewHolder, position: Int) {
        with(holder.binding){
            tvTag.text=list[position]
            ivRemove.setOnClickListener {
                list.removeAt(position)
                notifyItemRemoved(position)
            }

        }
    }

    override fun getItemCount()=list.size
    class ViewHolder(val binding:RowTagsBinding):RecyclerView.ViewHolder(binding.root) {

    }
}