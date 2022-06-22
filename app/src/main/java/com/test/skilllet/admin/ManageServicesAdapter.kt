package com.test.skilllet.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowManageServicesBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.*
import java.util.*

class ManageServicesAdapter(
    var context: Context,
    var list: ArrayList<WorkingServiceModel>,
    var visible: Int,
    var callBack: (size: Int) -> Unit
) : RecyclerView.Adapter<ManageServicesAdapter.ViewHolder>(), Filterable {

    var listFull = ArrayList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            RowManageServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            //tvDesc.text = list[position].description
            tvType.text = list[position].service?.type
            tvPrice.text = "${list[position].service?.price.toString()}"
            tvName.text = list[position].service?.name
            tvSpName.text = list[position].serviceProvider?.name
            tvDes.text = list[position].service?.description
            var str = ""
            for (s in list[position].service!!.tags) {
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
            btnReject.visibility = View.VISIBLE
            btnApprove.setOnClickListener {
                list[position].service?.offeringStatus = OfferingStatus.OFFERED.name
                val dialog = context.showProgressDialog("Accepting Service")
                dialog.show()
                Repository.addOrUpdateService(list[position].service!!) {
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
                if (list[position].service!!.offeringStatus == OfferingStatus.REQUESTED.name) {
                    startRejectionProcess(position)
                } else {
                    canRejectService(position)
                }


            }

            // ivIcon.setImageDrawable(list[position].icon)
        }
    }

    private fun canRejectService(position: Int) {
        var dialog=context.showProgressDialog("Checking Service Status")
        dialog.show()
        Repository.isServiceInProcess(list[position].service!!) { isItInProcess: Boolean ->
            dialog.cancel()
            if (!isItInProcess) {
                startRejectionProcess(position)
            } else {
                context.showDialogBox("Sorry This service is under process.") {}
            }
        }
    }

    private fun startRejectionProcess(position: Int) {
        context.showEditDialogBox("Rejection Reason", "Enter Reason Here") {
            if (it.trim().isNotEmpty()) {
                list[position].service!!.offeringStatus = OfferingStatus.REJECTED.name
                list[position].service!!.rejectionReason = it.trim()
                val dialog = context.showProgressDialog("Rejecting Service")
                dialog.show()
                Repository.addOrUpdateService(list[position].service!!) {
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
            } else {
                context.showToast("Failed to update")
            }
        }

    }

    override fun getItemCount() = list.size

    class ViewHolder(val binding: RowManageServicesBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
                    if (item.service!!.name.lowercase().contains(filterPattern) ||
                        item.service!!.tags.containsString(filterPattern)
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            listFull.clear()
            listFull.addAll(results.values as List<WorkingServiceModel>)
            notifyDataSetChanged()
        }

        fun ArrayList<String>.containsString(s: String): Boolean {
            for (str in this) {
                if (str.lowercase().contains(s)) {
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