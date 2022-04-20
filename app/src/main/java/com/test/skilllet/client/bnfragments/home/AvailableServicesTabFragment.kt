package com.test.skilllet.client.bnfragments.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.TempFragmentBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.showProgressDialog

class AvailableServicesTabFragment(var title: String):Fragment() {
    lateinit var binding: TempFragmentBinding
    var arr= arrayOf("Cleaning","Plumbing","Electrician")
     var list:ArrayList<ServiceModel>?=null



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
        binding.rv.layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        populateAdapter()

    }



    private fun populateAdapter() {
        var progressDialog=activity?.showProgressDialog("Please Wait","Loading Services")
        progressDialog?.show()
        when(title){
            arr[0]->{
                Repository.getAllCleaningServices {
                    progressDialog?.cancel()
                    setupAdapter(it)
                }

            }
            arr[1]->{
                Repository.getAllPlumbingServices {
                    progressDialog?.cancel()
                    setupAdapter(it)
                }
            }
            arr[2]->{
                Repository.getAllElectricianServices {
                    progressDialog?.cancel()
                    setupAdapter(it)
                }
            }
        }
    }

    private fun setupAdapter(it:ArrayList<ServiceModel>?){

        list=it
        if(list!=null&&list!!.size>0){
            var adapter= activity?.getDrawable(R.drawable.ic_cleaning)
                ?.let { AvailableServicesAdapter(list, it,title,requireActivity()) }
            binding.rv.adapter=adapter
        }else{

        }
    }

}