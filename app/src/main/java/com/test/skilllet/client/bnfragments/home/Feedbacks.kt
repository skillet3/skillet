package com.test.skilllet.client.bnfragments.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityFeedbacksBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.showProgressDialog
import com.test.skilllet.util.showToast

class Feedbacks : AppCompatActivity() {
    lateinit var binding:ActivityFeedbacksBinding
    lateinit var workingServiceModel: WorkingServiceModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFeedbacksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workingServiceModel=intent.getSerializableExtra("service") as WorkingServiceModel

        with(binding){
            Glide.with(this@Feedbacks).load(workingServiceModel.serviceProvider!!.url)
                .centerCrop()
                .placeholder(R.drawable.profile_charachter).into(ivProfile)
            tvName.text=workingServiceModel.serviceProvider?.name
            rbRating.rating=workingServiceModel.serviceProvider!!.rating
            rv.layoutManager=LinearLayoutManager(this@Feedbacks,LinearLayoutManager.VERTICAL,false)
            val dialog=this@Feedbacks.showProgressDialog("Loading Feedbacks")
            dialog.show()
            Repository.getFeedbacksForClients(workingServiceModel.service!!){list:ArrayList<WorkingServiceModel>?->
                dialog.cancel()
                if(!list.isNullOrEmpty()){
                    rv.adapter=FeedbackAdapter(this@Feedbacks,list)
                }else{
                    this@Feedbacks.showToast("Failed Operation")
                }
            }
        }


    }
}