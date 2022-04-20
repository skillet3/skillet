package com.test.skilllet.client.bnfragments.home

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityRequestServiceBinding
import com.test.skilllet.util.showProgressDialog

class RequestService : AppCompatActivity() {
    lateinit var binding:ActivityRequestServiceBinding
    var serviceType:String=""
    var serviceName:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRequestServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent?.let {
            serviceType= intent.getStringExtra("type").toString()
            serviceName= intent.getStringExtra("name").toString()
            binding.tvTypeName.text="$serviceType->$serviceName"
            getData()
        }


    }

    private fun getData() {
        var progress=this@RequestService.showProgressDialog("Please Wait",
            "Loading Services")
        progress.show()
        Repository.getServicesListByClient(serviceType,serviceName){
            progress.cancel()
            if(it!=null){
                var adapter=RequestServiceAdapter(it)
                binding.rv.layoutManager=LinearLayoutManager(this@RequestService,
                    LinearLayoutManager.VERTICAL,false)
                binding.rv.adapter=adapter
            }else{
                //TODO("NO DATA AVAILABLE")
            }
        }
    }
}