package com.test.skilllet.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.skilllet.databinding.ActivityHomeBinding
import com.test.skilllet.util.ServiceRequest
import com.test.skilllet.util.showMultiButtonDialogBox

class HomeActivity : AppCompatActivity() {
    lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.manAcc.setOnClickListener {
            startActivity(Intent(this@HomeActivity,ManageAccounts::class.java))
        }
        binding.manSer.setOnClickListener {
            startActivity(Intent(this@HomeActivity,ManageServices::class.java).apply {
                putExtra("status",ServiceRequest.OFFERED.name)
            })
        }

        binding.btnReq.setOnClickListener {
            startActivity(Intent(this@HomeActivity,ManageServices::class.java).apply {
                putExtra("status",ServiceRequest.REQUESTED.name)
            })
        }

    }

    override fun onBackPressed() {
        this@HomeActivity.showMultiButtonDialogBox("Are you sure you want to exit?"){
            if(it){
                finish()
            }
        }
    }
}