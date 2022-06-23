package com.test.skilllet.serviceprovider.history


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowProviderRequestHistoryBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.serviceprovider.paments.PaymentRequest
import com.test.skilllet.util.*
import java.util.*
import kotlin.collections.ArrayList


class SPServiceStatusAdapter(
    var context: Context, var list: ArrayList<WorkingServiceModel>,
    var canReject: Int,
    var canApprove: Int,
    var canDelete: Int,
    var canChat: Int,
    var canShowFeedback: Int,
    var canRequestPayment: Int
) :
    RecyclerView.Adapter<SPServiceStatusAdapter.ViewHolder>() ,Filterable{

    //comment
    var listFull=ArrayList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            RowProviderRequestHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            tvName.text = list[position].service!!.name
            tvType.text = list[position].service!!.type
            tvDes.text = list[position].service!!.description
            tvFeedback.text=list[position].serviceRequest?.feedbackByClient
            tvPrice.text=list[position].service!!.price
            tvSpName.text=list[position].client?.name
            tvDate.text="Date : ${list[position].serviceRequest?.date}"
            tvPass.text="Password : ${list[position].serviceRequest?.secretCode}"
            Glide.with(context).load(list[position].client?.url)
                .centerCrop()
                .placeholder(R.drawable.profile_charachter)
                .into(imageView11)
            rtProfile.rating=list[position].serviceRequest?.ratingByClient!!
            rtProfile.isEnabled=false
            btnDeleteRequest.visibility=canDelete
            tvFeedback.visibility=canShowFeedback
            tvFeedbackTag.visibility=canShowFeedback
            btnAcceptRequest.visibility=canApprove
            btnCancelRequest.visibility=canReject
            btnRequestPayment.visibility=canRequestPayment
            rtProfile.visibility=canShowFeedback
            var str = ""
            for (s in list[position].service!!.tags) {
                str += " , " + s
            }
            tvTags.text = str

            btnDeleteRequest.setOnClickListener {
                var dialog = context.showProgressDialog("Deleting Request")
                dialog.show()
                Repository.deleteServiceRequest(list[position].serviceRequest?.key){
                    dialog.cancel()
                    if (it) {
                        list.removeAt(position)
                        context.showToast("Request deleted Successfully")
                        notifyItemRemoved(position)
                    } else {
                        context.showToast("Could not delete Request.")
                    }
                }
            }
            btnAcceptRequest.setOnClickListener {
                var dialog = context.showProgressDialog("Accepting Request")
                dialog.show()
                list[position].serviceRequest?.serviceStatus=RequestStatus.APPROVED.name
                Repository.changeServiceRequestStatus(list[position].serviceRequest,RequestStatus.APPROVED.name){it:Boolean->
                    dialog.cancel()
                    if (it) {
                        sendNotification(list[position].client!!.token,"Request accepted",
                        "${Repository.loggedInUser!!.name} has accepted your request of ${list[position].service!!.name} service")

                        list.removeAt(position)
                        context.showToast("Request Accepted Successfully")
                        notifyItemRemoved(position)
                            } else {
                        context.showToast("Could not Accept Request.")
                    }
                }
            }
            btnCancelRequest.setOnClickListener {
                context.showEditDialogBox("Rejection Reason,","Enter Reason Here"){
                    if(it.isNotEmpty()){

                        var dialog = context.showProgressDialog("Rejecting Request")
                        dialog.show()
                        list[position].serviceRequest?.rejectionReason=it
                        list[position].serviceRequest?.serviceStatus=RequestStatus.DECLINE.name
                        Repository.changeServiceRequestStatus(list[position].serviceRequest,RequestStatus.DECLINE.name){it:Boolean->
                            dialog.cancel()
                            if (it) {
                                list.removeAt(position)
                                context.showToast("Request Rejected Successfully")
                                notifyItemRemoved(position)
                            } else {
                                context.showToast("Could not Reject Request.")
                            }
                        }
                    }else{

                    }
                }
            }
            btnRequestPayment.setOnClickListener {
                context?.startActivity(Intent(context,PaymentRequest::class.java).apply {
                    putExtra("service",list[position])
                })

                /*var dialog = context.showProgressDialog("Requesting Payment")
                dialog.show()
                Repository.changeServiceRequestPaymentStatus(list[position].serviceRequest?.key,PaymentStatus.REQUESTED.name){ it:Boolean->
                    dialog.cancel()
                    if (it) {
                        list.removeAt(position)
                        context.showToast("Successfully Sent Payment Request")
                        notifyItemRemoved(position)
                    } else {
                        context.showToast("Could not sent Payment Request.")
                    }
                }*/
            }
        }
    }

    override fun getItemCount() = list.size
    class ViewHolder(val binding: RowProviderRequestHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }
    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val filteredList: MutableList<WorkingServiceModel> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(listFull)
            } else {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in listFull) {
                    if (item.service!!.name.lowercase().contains(filterPattern)||
                        item.service!!.tags.containsString(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            list.clear()
            list.addAll(results.values as List<WorkingServiceModel>)
            notifyDataSetChanged()
        }

        fun ArrayList<String>.containsString(s:String):Boolean{
            for(str in this){
                if(str.lowercase().contains(s)){
                    return true
                }
            }
            return false
        }


    }

    override fun getFilter(): Filter {
        return exampleFilter
    }



}