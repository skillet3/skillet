package com.test.skilllet.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.skilllet.databinding.MainActivityBinding

class ClientActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}