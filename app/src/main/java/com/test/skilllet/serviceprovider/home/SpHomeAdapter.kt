package com.test.skilllet.serviceprovider.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.databinding.SpHomeRowBinding
import com.test.skilllet.models.ServiceModel

class SpHomeAdapter(var list:ArrayList<ServiceModel>):RecyclerView.Adapter<SpHomeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding=SpHomeRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            //tvDesc.text = list[position].description
            tvCategory.text=list[position].type
            tvPrice.text="$${list[position].price.toString()}"
            tvName.text=list[position].name

           // ivIcon.setImageDrawable(list[position].icon)
        }
    }

    override fun getItemCount()=list.size

    class ViewHolder(val binding: SpHomeRowBinding):RecyclerView.ViewHolder(binding.root) {

    }
}