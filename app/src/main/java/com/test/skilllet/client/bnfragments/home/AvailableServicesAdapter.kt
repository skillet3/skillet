package com.test.skilllet.client.bnfragments.home

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.databinding.RowHomeTempBinding
import com.test.skilllet.models.ServiceModel


open class AvailableServicesAdapter(var list:ArrayList<ServiceModel>?, var drawable: Drawable,var tabTitle:String,
var context: Context
):
    RecyclerView.Adapter<AvailableServicesAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding=RowHomeTempBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


       with(holder.binding){
           tvServiceName.text= list!![position].name
           ivIcon.setImageDrawable(drawable )
           tvPrice.text=list!![position].price
            root.setOnClickListener {
                context.startActivity(Intent(context,RequestService::class.java).apply {
                    putExtra("type",tabTitle)
                    putExtra("name",list!![position].name)
                })
            }
       }
    }

    override fun getItemCount()= list!!.size
    class ViewHolder(val binding: RowHomeTempBinding): RecyclerView.ViewHolder(binding.root) {

    }
}