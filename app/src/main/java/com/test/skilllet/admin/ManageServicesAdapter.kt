package com.test.skilllet.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowManageServicesBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.*

class ManageServicesAdapter(
    var context: Context,
    var list: ArrayList<WorkingServiceModel>,
    var visible: Int,
    var callBack:(size:Int)->Unit
) : RecyclerView.Adapter<ManageServicesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            RowManageServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            //tvDesc.text = list[position].description
            tvType.text = list[position].service.type
            tvPrice.text = "${list[position].service.price.toString()}"
            tvName.text = list[position].service.name
            tvSpName.text = list[position].serviceProvider?.name
            tvDes.text = list[position].service.description
            var str = ""
            for (s in list[position].service.tags) {
                str += " , " + s
            }
            tvTags.text = str
            Glide
                .with(context)
                .load(list[position].serviceProvider?.url)
                .centerCrop()
                .placeholder(R.drawable.profile_charachter)
                .into(imageView11)
            btnApprove.visibility = visible
            btnReject.visibility=visible
            btnApprove.setOnClickListener {
                list[position].service.offeringStatus = ServiceRequest.OFFERED.name
                val dialog=context.showProgressDialog("Accepting Service")
                dialog.show()
                Repository.addOrUpdateService(list[position].service) {
                    dialog.cancel()
                    if (it) {
                        list.removeAt(position)
                        notifyItemRemoved(position)
                        context.showToast("Successfully Updated");
                        callBack(list.size)
                    } else {
                        context.showToast("Could not update this item")
                    }
                }
            }

            btnReject.setOnClickListener {

                context.showEditDialogBox("Rejection Reason","Reason Here"){
                    if(it.trim().isNotEmpty()){
                        list[position].service.offeringStatus = ServiceRequest.REJECTED.name
                        list[position].service.rejectionReason=it.trim()
                        val dialog=context.showProgressDialog("Rejecting Service")
                            dialog.show()
                        Repository.addOrUpdateService(list[position].service) {
                            dialog.cancel()
                            if (it) {
                                list.removeAt(position)
                                notifyItemRemoved(position)
                                context.showToast("Successfully Updated");
                                callBack(list.size)
                            } else {
                                context.showToast("Could not update this item")
                            }
                        }
                    }else{
                        context.showToast("Failed to update")
                    }
                }


            }

            // ivIcon.setImageDrawable(list[position].icon)
        }
    }

    override fun getItemCount() = list.size

    class ViewHolder(val binding: RowManageServicesBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}