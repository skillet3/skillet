package com.test.skilllet.serviceprovider.paments

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.skilllet.databinding.ActivityPaymentRequestBinding

class PaymentRequest : AppCompatActivity() {

    lateinit var binding:ActivityPaymentRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityPaymentRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){

        }
    }
}