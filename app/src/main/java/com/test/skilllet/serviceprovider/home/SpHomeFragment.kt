package com.test.skilllet.serviceprovider.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.SpFragHomeBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.showProgressDialog

class SpHomeFragment : Fragment() {
    lateinit var binding: SpFragHomeBinding
    var list=ArrayList<ServiceModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SpFragHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

        binding.addService.setOnClickListener{
            activity?.startActivity(Intent(activity, AddServiceBYSP::class.java))
        }

    }

    private fun getData() {
        var dialog=activity?.showProgressDialog("Please Wait","Loading Offered Services")
        dialog?.show();
        Repository.getOfferedServicesBySp(Repository.loggedInUser){
            dialog?.cancel()
            if (it){
                list= ArrayList(Repository.allOfferedServices)
                var adapter= SpHomeAdapter(list)

                binding.rv.layoutManager=LinearLayoutManager(activity?.applicationContext,
                    LinearLayoutManager.VERTICAL,false)
                binding.rv.adapter=adapter
            }else{
                //TODO("no services availabel ka msg in layout")
            }
        }
    }
}