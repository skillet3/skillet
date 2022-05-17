package com.test.skilllet.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityManageServicesBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.serviceprovider.home.SpHomeAdapter
import com.test.skilllet.util.showProgressDialog

class ManageServices : AppCompatActivity() {
    lateinit var binding:ActivityManageServicesBinding
    var servicesList=ArrayList<ServiceModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityManageServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onStart() {
        super.onStart()
        var dialog=this.showProgressDialog("Please Wait","Loading Offered Services")
        dialog?.show();
        Repository.getServicesList {
        dialog.cancel()
            if (it != null) {
                servicesList=it
            }
            var adapter= ManageServicesAdapter(servicesList)

            binding.rv.layoutManager= LinearLayoutManager(this.applicationContext,
                LinearLayoutManager.VERTICAL,false)
            binding.rv.adapter=adapter
        }
    }
}