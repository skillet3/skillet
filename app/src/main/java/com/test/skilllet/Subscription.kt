package com.test.skilllet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.skilllet.client.bnfragments.profile.ProfileFragment
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivitySubscriptionBinding
import com.test.skilllet.databinding.ProfileFragmentBinding

class Subscription : AppCompatActivity() {

    lateinit var binding:ActivitySubscriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // val user= Repository.loggedInUser
        with(binding){
            btnLater.setOnClickListener{
                onBackPressed()
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}