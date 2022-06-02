package com.test.skilllet.serviceprovider.history


import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowHistoryTempBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.serviceprovider.paments.PaymentRequest


class SPServiceStatusAdapter (var context: Context, var visibility: Int, var list:ArrayList<ServiceModel>, var iconsList: ArrayList<Drawable>, @ColorInt var color: Int):
    RecyclerView.Adapter<SPServiceStatusAdapter.ViewHolder>() {

    //comment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding= RowHistoryTempBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            tvName.text=list[position].name
            ivIcon.setImageDrawable(iconsList[position])
            clIcon.setBackgroundColor(color)
            tvType.text=list[position].type
            tvDes.text=list[position].description
            btnRequestPayment.visibility=visibility
            btnRequestPayment.setOnClickListener {
                context.startActivity(Intent(context,PaymentRequest::class.java)
                    .putExtra("service",list[position]))
            }
        }
    }

    override fun getItemCount()= list.size
    class ViewHolder(val binding: RowHistoryTempBinding): RecyclerView.ViewHolder(binding.root) {

    }
}