package com.test.skilllet.admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowManageServicesBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.showDialogBox
import com.test.skilllet.util.showToast

class ManageServicesAdapter(var context: Context, var list:ArrayList<ServiceModel>):RecyclerView.Adapter<ManageServicesAdapter.ViewHolder>() {

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
                context.showDialogBox("You will be deleting the following activity\n" +
                        "Service Type: ${list[position].type}\n"+
                        "Service Name: ${list[position].name}\n"+
                        "Service Price: ${list[position].price}\n"){
                    Repository.deleteService(list[position]){
                        if (it){
                            list.removeAt(position)
                            notifyItemRemoved(position)
                        }else{
                            context.showToast("Could not delete this item")
                        }
                    }
                }


            }
            btnEdit.setOnClickListener {
                context.startActivity(Intent(context,AddService::class.java).apply {
                    putExtra("name",list[position].name)
                    putExtra("type",list[position].type)
                    putExtra("desc",list[position].description)
                    putExtra("price",list[position].price)
                })

            }

            // ivIcon.setImageDrawable(list[position].icon)
        }
    }

    override fun getItemCount()=list.size

    class ViewHolder(val binding: RowManageServicesBinding):RecyclerView.ViewHolder(binding.root) {

    }
}