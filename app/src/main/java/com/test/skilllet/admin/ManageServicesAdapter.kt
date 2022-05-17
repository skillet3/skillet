package com.test.skilllet.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.databinding.RowManageServicesBinding
import com.test.skilllet.models.ServiceModel

class ManageServicesAdapter(var list:ArrayList<ServiceModel>):RecyclerView.Adapter<ManageServicesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding= RowManageServicesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            //tvDesc.text = list[position].description
            tvCategory.text=list[position].type
            tvPrice.text="${list[position].price.toString()}"
            tvName.text=list[position].name

            btnDelete.setOnClickListener {

            }
            btnEdit.setOnClickListener {

            }

            // ivIcon.setImageDrawable(list[position].icon)
        }
    }

    override fun getItemCount()=list.size

    class ViewHolder(val binding: RowManageServicesBinding):RecyclerView.ViewHolder(binding.root) {

    }
}