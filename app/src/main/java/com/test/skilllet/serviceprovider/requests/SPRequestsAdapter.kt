package com.test.skilllet.serviceprovider.requests


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowHistoryTempBinding
import com.test.skilllet.databinding.SpRowRequestsBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.showProgressDialog


class SPRequestsAdapter (var context: Context, var list:ArrayList<ServiceModel>, var iconsList: ArrayList<Drawable>, @ColorInt var color: Int):
    RecyclerView.Adapter<SPRequestsAdapter.ViewHolder>() {

    //comment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding= SpRowRequestsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            tvName.text=list[position].name
            ivIcon.setImageDrawable(iconsList[position])
            clIcon.setBackgroundColor(color)
            tvType.text=list[position].type
            tvDes.text=list[position].description
            btnAccept.setOnClickListener {
                val progressDialog=context.showProgressDialog("Please Wait",
                "Accepting Request")
                progressDialog.show()
                Repository.insertOrUpdateServiceRequest(RequestStatus.APPROVED,list[position]){
                    progressDialog.cancel()
                }

            }
            btnReject.setOnClickListener {
                val progressDialog=context.showProgressDialog("Please Wait",
                    "Rejecting Request")
                progressDialog.show()
                Repository.insertOrUpdateServiceRequest(RequestStatus.DECLINE,list[position]){
                    progressDialog.cancel()
                }
            }

        }
    }

    override fun getItemCount()= list.size
    class ViewHolder(val binding: SpRowRequestsBinding): RecyclerView.ViewHolder(binding.root) {

    }
}