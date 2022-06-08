package com.test.skilllet.serviceprovider.history

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.TempFragmentBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.showProgressDialog

class SPServiceStatusFragment() : Fragment() {
    lateinit var binding: TempFragmentBinding

    lateinit var list: ArrayList<WorkingServiceModel>
    lateinit var progressDialog: ProgressDialog
    lateinit var status:String
    constructor( status: String):this(){
        this.status=status
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TempFragmentBinding.inflate(inflater, container, false)
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
        progressDialog.dismiss()
        binding.rv.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        when (status) {
            RequestStatus.PENDING.name -> {
                binding.rv.adapter = SPServiceStatusAdapter(
                    this.requireContext(),
                    list,
                    canReject = View.VISIBLE,
                    canApprove  =View.VISIBLE,
                    canDelete = View.GONE,
                    canChat = View.VISIBLE,
                    canShowFeedback = View.GONE,
                    canRequestPayment=View.GONE
                )
            }
            RequestStatus.APPROVED.name -> {
                binding.rv.adapter = SPServiceStatusAdapter(
                    this.requireContext(),
                    list,
                    canReject = View.GONE,
                    canApprove  =View.GONE,
                    canDelete = View.GONE,
                    canChat = View.VISIBLE,
                    canShowFeedback = View.GONE,
                    canRequestPayment=View.VISIBLE
                )
            }
            RequestStatus.COMPLETED.name -> {
                binding.rv.adapter =
                    SPServiceStatusAdapter(
                        this.requireContext(),
                        list,
                        canReject = View.GONE,
                        canApprove  =View.GONE,
                        canDelete = View.GONE,
                        canChat = View.GONE,
                        canShowFeedback = View.VISIBLE,
                        canRequestPayment=View.GONE
                    )
            }
            RequestStatus.DECLINE.name -> {
                binding.rv.adapter = SPServiceStatusAdapter(
                    this.requireContext(),
                    list,
                    canApprove  =View.GONE,
                    canReject = View.GONE,
                    canDelete = View.VISIBLE,
                    canChat = View.GONE,
                    canShowFeedback = View.GONE,
                    canRequestPayment=View.GONE
                )
            }
        }


    }

    private fun getList() {
        progressDialog.show()
        Repository.getServiceByProviderAndStatus(
            status,
            Repository.loggedInUser!!.key
        ) { arrayList ->
            progressDialog.cancel()
            initList(arrayList)
        }
    }


    fun initList(arrayList: ArrayList<WorkingServiceModel>?) {
        progressDialog.cancel()
        if (arrayList != null) {
            list = ArrayList(arrayList)
            setupAdapter()
            binding.rv.visibility = View.VISIBLE
            binding.ivEmpty.visibility = View.GONE
        } else {
            binding.rv.visibility = View.GONE
            binding.ivEmpty.visibility = View.VISIBLE

        }
    }

}