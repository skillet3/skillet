package com.test.skilllet.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityManageServicesBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.ServiceRequest
import com.test.skilllet.util.showProgressDialog

class ManageServices : AppCompatActivity() {
    lateinit var binding:ActivityManageServicesBinding
    var status=ServiceRequest.REQUESTED.name;
    var servicesList=ArrayList<WorkingServiceModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityManageServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent?.getStringExtra("status")?.equals(ServiceRequest.OFFERED.name) == true){
            status=ServiceRequest.OFFERED.name;
        }else{
            status=ServiceRequest.REQUESTED.name
        }

    }

    override fun onResume() {
        super.onResume()
        init()
    }

    fun init(){
        if(!servicesList.isEmpty()){
            return
        }
        var dialog=this.showProgressDialog("Please Wait","Loading Offered Services")
        dialog.show();
        Repository.getServicesByType(status) {
            dialog.cancel()
            if (it != null&&it.isNotEmpty()) {
                servicesList=it
                var adapter= ManageServicesAdapter(this@ManageServices,servicesList,
                    if(status==ServiceRequest.OFFERED.name){
                        View.GONE
                    }else{
                        View.VISIBLE
                    }){
                    if(it!=0){
                        binding.ivEmpty.visibility= View.GONE
                        binding.rv.visibility=View.VISIBLE
                    }else{
                        binding.ivEmpty.visibility= View.VISIBLE
                        binding.rv.visibility=View.GONE
                    }
                }

                binding.rv.layoutManager= LinearLayoutManager(this.applicationContext,
                    LinearLayoutManager.VERTICAL,false)
                binding.rv.adapter=adapter
                binding.ivEmpty.visibility= View.GONE
                binding.rv.visibility=View.VISIBLE
            }else{
                binding.ivEmpty.visibility= View.VISIBLE
                binding.rv.visibility=View.GONE
            }

        }
    }
}