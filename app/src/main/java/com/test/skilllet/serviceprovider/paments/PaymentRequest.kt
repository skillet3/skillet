package com.test.skilllet.serviceprovider.paments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityPaymentRequestBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.*

class PaymentRequest : AppCompatActivity() {

    lateinit var binding: ActivityPaymentRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var accType=""
        with(binding) {
            var workingServiceModel: WorkingServiceModel =
                intent?.getSerializableExtra("service") as WorkingServiceModel
            accType=Repository.loggedInUser!!.accType
            tvName.text = if(accType=="Client"){
                workingServiceModel.serviceProvider?.name
            }else{
                workingServiceModel.client?.name
            }
            tvAddress.text =if(accType=="Client"){
                if (workingServiceModel.serviceProvider?.address.isNullOrEmpty())
                    "No Address Mentioned"
                else
                    workingServiceModel.serviceProvider?.address
            }else{
                if (workingServiceModel.client?.address.isNullOrEmpty())
                    "No Address Mentioned"
                else
                    workingServiceModel.client?.address
            }

            Glide.with(this@PaymentRequest)
                .load(workingServiceModel.client?.url)
                .centerCrop()
                .placeholder(R.drawable.profile_charachter)
                .into(imageView)
            tvPrice.text = workingServiceModel.service!!.price
            tvType.text = workingServiceModel.service!!.type
            tvServiceName.text = workingServiceModel.service!!.name

            btnRequestPayment.text=if(accType=="Client"){
                "Confirm Payment"
            }else{
                "Request Payment"
            }

            btnRequestPayment.setOnClickListener {
                if (ratingBar.rating == 0.0f) {
                    this@PaymentRequest.showToast("Please rate this person")
                    return@setOnClickListener
                }
                if (etFeedback.text.isEmpty()) {
                    this@PaymentRequest.showToast("Please give your feedback")
                    return@setOnClickListener
                }
                val user=if(accType=="Client"){
                    workingServiceModel.serviceProvider
                }else{
                    workingServiceModel.client
                }
                var newRating=((user?.rating?.times(user?.totalFeedbacks!!))?.plus(ratingBar.rating))
                user?.totalFeedbacks?.inc()
                user?.rating= user?.totalFeedbacks?.let { it1 ->
                    newRating?.div(
                        it1
                    )
                }!!
                if(accType=="Client"){
                    workingServiceModel.serviceProvider=user
                    workingServiceModel.serviceRequest?.paymentStatus= PaymentStatus.PAYED.name
                    workingServiceModel.serviceRequest?.serviceStatus=RequestStatus.COMPLETED.name
                    workingServiceModel.serviceRequest?.feedbackByClient= etFeedback.text.toString()
                    workingServiceModel.serviceRequest?.ratingByClient=ratingBar.rating
                }else{
                    workingServiceModel.client=user
                    workingServiceModel.serviceRequest?.paymentStatus= PaymentStatus.REQUESTED.name
                    workingServiceModel.serviceRequest?.feedbackByProvider= etFeedback.text.toString()
                    workingServiceModel.serviceRequest?.ratingByProvider=user.rating
                }


//                this@PaymentRequest.sendNotification(service.user?.token!!,"Payment Request","${Repository.loggedInUser?.name} has" +
//                        "requested payment for ${service.name} service.")
//              d
                val dialog=this@PaymentRequest.showProgressDialog("Please Wait")
                dialog.show()
                Repository.updateServiceRequest(
                    workingServiceModel.serviceRequest!!
                ){
                    if(it){

                        Repository.updateUser(user){it:Boolean->
                            dialog.cancel()
                            this@PaymentRequest.showToast("Payment Request Sent/Confirmed Successfully")
                            onBackPressed()
                        }


                    }else{
                        this@PaymentRequest.showToast("Failed to send/confirm Payment Request")
                    }
                }

            }
            btnBack.setOnClickListener {
               onBackPressed()
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}