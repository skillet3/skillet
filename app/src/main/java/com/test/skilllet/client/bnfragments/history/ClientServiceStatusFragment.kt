package com.test.skilllet.client.bnfragments.history

import android.app.ProgressDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.R
import com.test.skilllet.database.Repository

import com.test.skilllet.databinding.TempFragmentBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.ViewType
import com.test.skilllet.util.showProgressDialog
import java.security.interfaces.RSAKey
import javax.net.ssl.SSLEngineResult

class ClientServiceStatusFragment(var status: String) : Fragment() {
    lateinit var binding: TempFragmentBinding


    lateinit var list: ArrayList<WorkingServiceModel>
    lateinit var progressDialog: ProgressDialog
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
        progressDialog = requireActivity().showProgressDialog("Please Wait", "Loading Services}")
        getList()

    }



    private fun setupAdapter() {
        progressDialog.dismiss()
        binding.rv.layoutManager=LinearLayoutManager(this.requireContext(),LinearLayoutManager.VERTICAL,false)
        when(status){
            RequestStatus.PENDING.name->{
                binding.rv.adapter=ClientServiceStatusAdapter(list,View.VISIBLE,View.VISIBLE)
            }
            RequestStatus.APPROVED.name->{
                binding.rv.adapter=ClientServiceStatusAdapter(list,View.VISIBLE,View.GONE)
            }
            RequestStatus.COMPLETED.name->{
                binding.rv.adapter=ClientServiceStatusAdapter(list,View.GONE,View.GONE)

            }
            RequestStatus.DECLINE.name->{
                binding.rv.adapter=ClientServiceStatusAdapter(list,View.GONE,View.VISIBLE)

            }
        }


    }

    private fun getList() {
        progressDialog.show()
        Repository.getServiceByClientAndStatus(status,Repository.loggedInUser!!.key) { arrayList ->
            initList(arrayList)
        }
    }


    fun initList(arrayList: ArrayList<WorkingServiceModel>?) {
        progressDialog.cancel()
        if (arrayList != null) {
            list = ArrayList(arrayList)
            setupAdapter()
            binding.rv.visibility=View.VISIBLE
            binding.ivEmpty.visibility=View.GONE
        } else {
            binding.rv.visibility=View.GONE
            binding.ivEmpty.visibility=View.VISIBLE

        }
    }

}