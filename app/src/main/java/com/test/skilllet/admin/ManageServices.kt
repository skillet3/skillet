package com.test.skilllet.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityManageServicesBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.OfferingStatus
import com.test.skilllet.util.showProgressDialog

class ManageServices : AppCompatActivity() {
    lateinit var binding:ActivityManageServicesBinding
    var status=OfferingStatus.REQUESTED.name;
    var servicesList=ArrayList<WorkingServiceModel>()

    var adapter:ManageServicesAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityManageServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent?.getStringExtra("status")?.equals(OfferingStatus.OFFERED.name) == true){
            status=OfferingStatus.OFFERED.name;
        }else{
            status=OfferingStatus.REQUESTED.name
        }
        val searchView: SearchView = binding.searchView
        searchView.visibility=View.VISIBLE
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.getFilter()?.filter(newText)
                return false
            }
        })
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
        Repository.getServicesByOfferingStatus(status) {
            dialog.cancel()
            if (it != null&&it.isNotEmpty()) {
                servicesList=it
                adapter= ManageServicesAdapter(this@ManageServices,servicesList,
                    if(status==OfferingStatus.OFFERED.name){
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