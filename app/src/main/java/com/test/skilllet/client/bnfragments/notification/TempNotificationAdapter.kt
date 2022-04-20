package com.test.skilllet.client.bnfragments.notification


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.databinding.RowHistoryTempBinding
import com.test.skilllet.databinding.RowNotificationBinding


class TempNotificationAdapter (var list:ArrayList<String>, var iconsList: ArrayList<Drawable>):
    RecyclerView.Adapter<TempNotificationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding= RowNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            //binding.tvDesc.text=list[position]
            binding.clIcon.background=iconsList[position]
        }
    }

    override fun getItemCount()= list.size
    class ViewHolder(val binding: RowNotificationBinding): RecyclerView.ViewHolder(binding.root) {

    }
}