package com.test.skilllet.serviceprovider.history

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
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.ViewType
import com.test.skilllet.util.showProgressDialog

class SPServiceStatusFragment(var status: RequestStatus) : Fragment() {
    lateinit var binding: TempFragmentBinding

    var services = arrayOf("cleaning", "plumbing", "electrician")
    lateinit var list: ArrayList<ServiceModel>
    var listIcons = ArrayList<Drawable>()
    lateinit var cleaningDrawable: Drawable
    lateinit var plumbingDrawable: Drawable
    lateinit var electricianDrawable: Drawable
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
        cleaningDrawable = activity?.getDrawable(R.drawable.ic_cleaning)!!
        plumbingDrawable = activity?.getDrawable(R.drawable.ic_plumbing)!!
        electricianDrawable = activity?.getDrawable(R.drawable.ic_electrician)!!
        progressDialog = requireActivity().showProgressDialog("Please Wait", "Loading History")
        getList()

    }

    private fun initIconsList() {
        for (service in list) {
            if (service.type == services[0]) {
                listIcons.add(cleaningDrawable)
            } else if (service.type == services[1]) {
                listIcons.add(plumbingDrawable)
            } else if (service.type == services[2]) {
                listIcons.add(electricianDrawable)
            }
        }
    }

    private fun setupAdapter() {

        initIconsList();

        when (status.name) {
            RequestStatus.APPROVED.name -> {
                var adapter = activity?.resources?.getColor(R.color.approved)
                    ?.let { SPServiceStatusAdapter(requireActivity(),View.VISIBLE,list, listIcons, it) }
                binding.rv.adapter = adapter
            }
            RequestStatus.COMPLETED.name -> {
                var adapter = activity?.resources?.getColor(R.color.completed)
                    ?.let { SPServiceStatusAdapter(requireActivity(),View.GONE,list, listIcons, it) }
                binding.rv.adapter = adapter
            }
            RequestStatus.DECLINE.name -> {
                var adapter = activity?.resources?.getColor(R.color.completed)
                    ?.let { SPServiceStatusAdapter(requireActivity(),View.GONE,list, listIcons, it) }
                binding.rv.adapter = adapter
            }
        }
        progressDialog.dismiss()
    }

    private fun getList() {
        progressDialog.show()
        Repository.getServices(ViewType.SERVICE_PROVIDER,status) { arrayList ->
            progressDialog.dismiss()
            initList(arrayList)
        }
    }


    fun initList(arrayList: ArrayList<ServiceModel>?) {

        if (arrayList != null) {
            list = ArrayList(arrayList)
            setupAdapter()
        } else {
            progressDialog.cancel()
        }
    }

}