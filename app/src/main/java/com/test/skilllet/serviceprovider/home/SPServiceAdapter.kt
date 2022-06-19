package com.test.skilllet.serviceprovider.home


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowSpServiceAdapterBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.*


class SPServiceAdapter(
    var context: Context, var list: ArrayList<ServiceModel>, var visible: Int,
    var callBack:(size:Int)->Unit) :
    RecyclerView.Adapter<SPServiceAdapter.ViewHolder>() {

    //comment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            RowSpServiceAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            tvName.text = list[position].name
            tvType.text = list[position].type
            tvDes.text = list[position].description
            tvPrice.text = list[position].price
            btnDelete.visibility = visible
            btnEdit.visibility = visible
            tvReason.visibility = visible
            tvr.visibility = visible
            tvReason.text=list[position].rejectionReason
            btnEdit.setOnClickListener {
                if(list[position].offeringStatus==OfferingStatus.OFFERED.name){
                    canEditService(position)
                }else{
                    context.startActivity(Intent(context,AddServiceBYSP::class.java).apply {
                        putExtra("service",list[position])
                    })
                }
            }

            btnDelete.setOnClickListener {
                if(list[position].offeringStatus==OfferingStatus.OFFERED.name){
                    canDeleteService(position)
                }else{
                    deleteService(position)
                }
            }
        }
    }

    override fun getItemCount() = list.size
    class ViewHolder(val binding: RowSpServiceAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    private fun canEditService(position: Int) {
        var dialog=context.showProgressDialog("Checking Service Status")
        dialog.show()
        Repository.isServiceInProcess(list[position]) { isItInProcess: Boolean ->
            dialog.cancel()
            if (!isItInProcess) {
                context.startActivity(Intent(context,AddServiceBYSP::class.java).apply {
                    putExtra("service",list[position])
                })
            } else {
                context.showDialogBox("Sorry This service is under process.") {}
            }
        }
    }
    private fun canDeleteService(position: Int) {
        var dialog=context.showProgressDialog("Checking Service Status")
        dialog.show()
        Repository.isServiceInProcess(list[position]) { isItInProcess: Boolean ->
            dialog.cancel()
            if (!isItInProcess) {
                deleteService(position)
            } else {
                context.showDialogBox("Sorry This service is under process.") {}
            }
        }
    }

    fun deleteService(position: Int){
        context.showMultiButtonDialogBox("Are you sure you want to delete this Service?","Deleting Service"){
            if(it){
                val dialog=context.showProgressDialog("Deleting Service")
                dialog.show()
                Repository.deleteService(list[position]){
                    dialog.cancel()
                    if(it){
                        list.removeAt(position)
                        callBack(list.size)
                        notifyItemRemoved(position)
                    }else{
                        context.showToast("Failed to delete")
                    }
                }
            }
        }
    }
}