package com.test.skilllet.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.core.Repo
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityManageServiceTypesBinding
import com.test.skilllet.util.showToast

class ManageServiceTypes : AppCompatActivity() {

    lateinit var binding:ActivityManageServiceTypesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityManageServiceTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        Repository.getListOfServiceTypes {
            if(!it.isNullOrEmpty()){
                binding.rv.adapter=ManageSerTypesAdapter(this,it!!)
            }else{
                this.showToast("No Service Types Available")
            }
        }

    }
}