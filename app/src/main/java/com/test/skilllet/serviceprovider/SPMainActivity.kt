package com.test.skilllet.serviceprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.skilllet.databinding.ActivityMainSpBinding


class SPMainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainSpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainSpBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}