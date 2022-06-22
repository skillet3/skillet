package com.test.skilllet.client.bnfragments.home

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
import com.test.skilllet.databinding.RowHomeTempBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.*
import java.util.*


open class AvailableServicesAdapter(
    var list: ArrayList<WorkingServiceModel>,
    var context: Context
) :
    RecyclerView.Adapter<AvailableServicesAdapter.ViewHolder>() , Filterable {

    var tempList= ArrayList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = RowHomeTempBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        with(holder.binding) {
            //tvDesc.text = list[position].description
            tvType.text = tempList[position].service!!.type
            tvPrice.text = "${tempList[position].service!!.price.toString()}"
            tvName.text = tempList[position].service!!.name
            tvSpName.text = tempList[position].serviceProvider?.name
            tvDes.text = tempList[position].service!!.description
            var str = ""
            for (s in tempList[position].service!!.tags) {
                str += " , " + s
            }
            tvTags.text = str
            Glide
                .with(context)
                .load(tempList[position].serviceProvider?.url)
                .centerCrop()
                .placeholder(R.drawable.profile_charachter)
                .into(imageView11)


            btnRequest.setOnClickListener {
                context.showDialoguePickerDialogue(getDate(System.currentTimeMillis()), getTime(System.currentTimeMillis())){
                    if(it.isNotEmpty()){
                        var date=it
                        var dialog = context.showProgressDialog("Sending Request")
                        dialog.show()
                        Repository.sendRequest(tempList[position],date) { it1: Boolean ->
                            dialog.cancel()
                            if (it1) {
                                sendNotification(tempList[position].serviceProvider!!.token,
                                title = "Service Request","${Repository.loggedInUser!!.name}" +
                                            " has sent you a service request.")
                                context.showToast("Request Sent Successfully")
                            } else {
                                context.showToast("Could not sent Request.")
                            }
                        }
                    }else{
                        context.showToast("No Date picked")
                    }
                }





            }
            btnMore.setOnClickListener {
                context.startActivity(
                    Intent(context,Feedbacks::class.java)
                        .putExtra("service",tempList[position]))
            }
            // ivIcon.setImageDrawable(list[position].icon)
        }
    }

    override fun getItemCount() = tempList!!.size
    class ViewHolder(val binding: RowHomeTempBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    private val exampleFilter: Filter = object : Filter() {
         override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val filteredList: MutableList<WorkingServiceModel> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(list)
            } else {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in list) {
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
            tempList.clear()
            tempList.addAll(results.values as List<WorkingServiceModel>)
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