package com.test.skilllet.serviceprovider.history


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowProviderRequestHistoryBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.serviceprovider.paments.PaymentRequest
import com.test.skilllet.util.PaymentStatus
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.showProgressDialog
import com.test.skilllet.util.showToast


class SPServiceStatusAdapter(
    var context: Context, var list: ArrayList<WorkingServiceModel>,
    var canReject: Int,
    var canApprove: Int,
    var canDelete: Int,
    var canChat: Int,
    var canShowFeedback: Int,
    var canRequestPayment: Int
) :
    RecyclerView.Adapter<SPServiceStatusAdapter.ViewHolder>() {

    //comment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            RowProviderRequestHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            tvName.text = list[position].service.name
            tvType.text = list[position].service.type
            tvDes.text = list[position].service.description
            tvFeedback.text=list[position].serviceRequest?.feedbackByClient
            tvPrice.text=list[position].service.price
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
            for (s in list[position].service.tags) {
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
                Repository.changeServiceRequestStatus(list[position].serviceRequest?.key,RequestStatus.APPROVED.name){it:Boolean->
                    dialog.cancel()
                    if (it) {
                        list.removeAt(position)
                        context.showToast("Request Accepted Successfully")
                        notifyItemRemoved(position)
                    } else {
                        context.showToast("Could not Accept Request.")
                    }
                }
            }
            btnCancelRequest.setOnClickListener {
                var dialog = context.showProgressDialog("Rejecting Request")
                dialog.show()
                Repository.changeServiceRequestStatus(list[position].serviceRequest?.key,RequestStatus.DECLINE.name){it:Boolean->
                    dialog.cancel()
                    if (it) {
                        list.removeAt(position)
                        context.showToast("Request Rejected Successfully")
                        notifyItemRemoved(position)
                    } else {
                        context.showToast("Could not Reject Request.")
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
}