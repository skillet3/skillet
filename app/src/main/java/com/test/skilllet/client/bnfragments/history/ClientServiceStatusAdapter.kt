package com.test.skilllet.client.bnfragments.history


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
import com.test.skilllet.databinding.RowHistoryTempBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.serviceprovider.paments.PaymentRequest
import com.test.skilllet.util.PaymentStatus
import com.test.skilllet.util.showProgressDialog
import com.test.skilllet.util.showToast
import java.util.*
import kotlin.collections.ArrayList


class ClientServiceStatusAdapter(
    var context: Context,
    var list: ArrayList<WorkingServiceModel>,
    var canChat: Int,var canCancel: Int,
    var canShowFeedback:Int,
    var canConfirmPayment:Int
) :
    RecyclerView.Adapter<ClientServiceStatusAdapter.ViewHolder>(), Filterable {

    var listFull=ArrayList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            RowHistoryTempBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            tvName.text = list[position].service.name
            tvType.text = list[position].service.type
            tvDes.text = list[position].service.description
            tvFeedback.text=list[position].serviceRequest?.feedbackByProvider
            tvPrice.text=list[position].service.price
            tvSpName.text=list[position].serviceProvider?.name
            tvPass.text="Password : ${list[position].serviceRequest?.secretCode}"
            tvDate.text="Date : ${list[position].serviceRequest?.date}"
            var str = ""
            for (s in list[position].service.tags) {
                str += " , " + s
            }
            tvTags.text = str
            Glide.with(context).load(list[position].serviceProvider?.url)
                .centerCrop()
                .placeholder(R.drawable.profile_charachter)
                .into(imageView11)

            rtProfile.rating=list[position].serviceRequest?.ratingByProvider!!
            rtProfile.isEnabled=false
            btnDeleteRequest.visibility=canCancel
            tvFeedback.visibility=canShowFeedback
            tvFeedbackTag.visibility=canShowFeedback
            btnConfirmPayment.visibility=canConfirmPayment
            rtProfile.visibility=canShowFeedback

            btnConfirmPayment.isEnabled= list[position].serviceRequest?.paymentStatus == PaymentStatus.REQUESTED.name
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
            btnConfirmPayment.setOnClickListener {

                context?.startActivity(Intent(context,PaymentRequest::class.java).apply {
                    putExtra("service",list[position])
                })


//                var dialog = context.showProgressDialog("Confirm Payment")
//                dialog.show()
//                Repository.deleteServiceRequest(list[position].serviceRequest?.key){
//                    dialog.cancel()
//                    if (it) {
//                        list.removeAt(position)
//                        context.showToast("Request deleted Successfully")
//                        notifyItemRemoved(position)
//                    } else {
//                        context.showToast("Could not delete Request.")
//                    }
//                }
            }
        }
    }

    override fun getItemCount() = list.size
    class ViewHolder(val binding: RowHistoryTempBinding) : RecyclerView.ViewHolder(binding.root) {

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
                    if (item.service.name.lowercase().contains(filterPattern)||
                        item.service.tags.containsString(filterPattern)) {
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