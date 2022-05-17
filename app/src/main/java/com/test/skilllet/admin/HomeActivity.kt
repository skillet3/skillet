package com.test.skilllet.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.skilllet.R
import com.test.skilllet.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.manAcc.setOnClickListener {

        }
        binding.manSer.setOnClickListener {
            startActivity(Intent(this@HomeActivity,ManageServices::class.java))
        }

    }
}