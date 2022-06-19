package com.test.skilllet.serviceprovider.home

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.database.Repository

import com.test.skilllet.databinding.TempFragmentBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.OfferingStatus
import com.test.skilllet.util.showProgressDialog

class SPServicesFragment() : Fragment() {
    lateinit var binding: TempFragmentBinding

    lateinit var list: ArrayList<ServiceModel>

    lateinit var progressDialog: ProgressDialog
     var status:String=OfferingStatus.OFFERED.name
    constructor( status: String):this(){
        this.status=status
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TempFragmentBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        progressDialog = requireActivity().showProgressDialog("Please Wait", "Loading History")



    }

    override fun onResume() {
        super.onResume()
        getList()
    }

    private fun setupAdapter() {
        binding.rv.layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        binding.rv.adapter=SPServiceAdapter(this.requireContext(),list,if(status==OfferingStatus.REJECTED.name||
                status==OfferingStatus.OFFERED.name){
            View.VISIBLE
        }else{
            View.GONE
        }){

        }
    }

    private fun getList() {
        progressDialog.show()
        Repository.getServicesOffered(status,Repository.loggedInUser!!.key) { arrayList:ArrayList<ServiceModel>? ->
            progressDialog.dismiss()
            if(arrayList!=null&&arrayList.isNotEmpty()){
                binding.ivEmpty.visibility=View.GONE
                binding.rv.visibility=View.VISIBLE
                list=arrayList
                setupAdapter()
            }else{
                binding.ivEmpty.visibility=View.VISIBLE
                binding.rv.visibility=View.GONE
            }
        }
    }




}