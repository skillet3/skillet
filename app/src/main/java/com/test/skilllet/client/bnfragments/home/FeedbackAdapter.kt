package com.test.skilllet.client.bnfragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.databinding.RowFeedbackBinding
import com.test.skilllet.models.WorkingServiceModel

class FeedbackAdapter(val context: Context, val list:ArrayList<WorkingServiceModel>):RecyclerView.Adapter<FeedbackAdapter.MyViewHolder>() {

    class MyViewHolder(val binding:RowFeedbackBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=RowFeedbackBinding.inflate(LayoutInflater.from(parent.context),
        parent,false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder.binding){
            tvClientName.text=list[position].client?.name
            Glide.with(context).load(list[position].client!!.url).centerCrop()
                .placeholder(R.drawable.profile_charachter)
                .into(ivProfile)
            rbClientRating.rating=list[position].serviceRequest!!.ratingByClient
            tvClientFeedback.text=list[position].serviceRequest!!.feedbackByClient
        }
    }

    override fun getItemCount()=list.size
}