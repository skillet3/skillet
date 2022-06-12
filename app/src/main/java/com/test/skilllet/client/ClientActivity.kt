package com.test.skilllet.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.MainActivityBinding
import com.test.skilllet.util.showMultiButtonDialogBox

class ClientActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        Repository.loggedInUser=null
        super.onDestroy()
    }

    override fun onBackPressed() {
        this@ClientActivity.showMultiButtonDialogBox("Are you sure you want to Logout?"){
            if(it){
                finish()
            }
        }
    }
}