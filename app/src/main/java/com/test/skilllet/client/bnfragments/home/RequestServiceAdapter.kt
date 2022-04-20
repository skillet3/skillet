package com.test.skilllet.client.bnfragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ClinetRowRequestServiceBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.showProgressDialog

class RequestServiceAdapter (var context: Context,var list:ArrayList<ServiceModel>):RecyclerView.Adapter<
        RequestServiceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding=ClinetRowRequestServiceBinding.inflate(LayoutInflater.from(parent.context),
        parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            tvName.text=list[position].user?.name
            tvDesc.text=list[position].description
            tvPrice.text=list[position].price
            btnRequest.setOnClickListener {
                    var progress=context.showProgressDialog("Please Wait",
                    "Sending Request")
                progress.show()
                Repository.insertServiceRequest(list[position]){isSuccessful:Boolean->
                    progress.cancel()
                    if(isSuccessful){
                        btnRequest.isEnabled=false
                        btnRequest.text="Sent"
                        Toast.makeText(context,"Request sent",Toast.LENGTH_SHORT).show();
                    }else{

                    }
                }
            }
        }
    }

    override fun getItemCount()= list.size
    class ViewHolder(var binding:ClinetRowRequestServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}