package com.test.skilllet.client.bnfragments.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.TempFragmentBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.showProgressDialog

class AvailableServicesTabFragment(var title: String):Fragment() {
    lateinit var binding: TempFragmentBinding
     var list:ArrayList<WorkingServiceModel>?=null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= TempFragmentBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.layoutManager=LinearLayoutManager(this.requireContext(),LinearLayoutManager.VERTICAL,false)
        populateAdapter()

    }



    private fun populateAdapter() {
        var progressDialog= this.requireContext().showProgressDialog("Please Wait","Loading Services")
        progressDialog.show()
        Repository.getServicesByServiceType(title){
            progressDialog.cancel()
            setupAdapter(it)
        }
    }

    private fun setupAdapter(it:ArrayList<WorkingServiceModel>?){
        list=it
        if(list!=null&&list!!.size>0){
            var adapter= AvailableServicesAdapter(list!!,this.requireContext())
            binding.rv.adapter=adapter
            binding.ivEmpty.visibility=View.GONE
            binding.rv.visibility=View.VISIBLE
        }else{
            binding.ivEmpty.visibility=View.VISIBLE
            binding.rv.visibility=View.GONE
        }
    }

}