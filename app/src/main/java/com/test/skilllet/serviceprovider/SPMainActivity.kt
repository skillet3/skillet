package com.test.skilllet.serviceprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityMainSpBinding
import com.test.skilllet.util.showMultiButtonDialogBox


class SPMainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainSpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainSpBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onDestroy() {
        Repository.loggedInUser=null
        super.onDestroy()
    }

    override fun onBackPressed() {
        this@SPMainActivity.showMultiButtonDialogBox("Are you sure you want to Logout?"){
            if(it){
                finish()
            }
        }
    }
}