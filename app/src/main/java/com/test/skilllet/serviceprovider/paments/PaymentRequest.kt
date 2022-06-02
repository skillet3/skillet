package com.test.skilllet.serviceprovider.paments

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.core.Repo
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityPaymentRequestBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.PaymentStatus
import com.test.skilllet.util.sendNotification
import com.test.skilllet.util.showToast

class PaymentRequest : AppCompatActivity() {

    lateinit var binding:ActivityPaymentRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityPaymentRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            var service:ServiceModel=intent?.getSerializableExtra("service") as ServiceModel
            tvName.text=service.user?.name
            tvAddress.text=service.user?.address
            tvSpName.text=Repository.loggedInUser?.name
            tvPrice.text=service.price
            tvType.text=service.type
            tvServiceName.text=service.name

            btnRequestPayment.setOnClickListener {
                if(ratingBar.rating==0.0f){
                    this@PaymentRequest.showToast("Please rate this person")
                    return@setOnClickListener
                }
                if( etFeedback.text.isEmpty()){
                    this@PaymentRequest.showToast("Please give your feedback")
                    return@setOnClickListener
                }

                var newRating=((service.user?.rating?.times(service.user?.totalFeedbacks!!))?.plus(ratingBar.rating))
                service?.user?.totalFeedbacks?.inc()
                service?.user?.rating= service?.user?.totalFeedbacks?.let { it1 ->
                    newRating?.div(
                        it1
                    )
                }!!
                service.paymentStatus=PaymentStatus.REQUESTED.value
                this@PaymentRequest.sendNotification(service.user?.token!!,"Payment Request","${Repository.loggedInUser?.name} has" +
                        "requested payment for ${service.name} service.")



            }
            btnBack.setOnClickListener{
                finish()
            }
        }


    }
}