package com.test.skilllet.client.bnfragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowHomeTempBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.showProgressDialog
import com.test.skilllet.util.showToast


open class AvailableServicesAdapter(
    var list: ArrayList<WorkingServiceModel>,
    var context: Context
) :
    RecyclerView.Adapter<AvailableServicesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = RowHomeTempBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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


            btnRequest.setOnClickListener {

                var dialog = context.showProgressDialog("Sending Request")
                dialog.show()
                Repository.sendRequest(list[position]) { it: Boolean ->
                    dialog.cancel()
                    if (it) {
                        context.showToast("Request Sent Successfully")
                    } else {
                        context.showToast("Could not sent Request.")
                    }
                }
            }
            // ivIcon.setImageDrawable(list[position].icon)
        }
    }

    override fun getItemCount() = list!!.size
    class ViewHolder(val binding: RowHomeTempBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}