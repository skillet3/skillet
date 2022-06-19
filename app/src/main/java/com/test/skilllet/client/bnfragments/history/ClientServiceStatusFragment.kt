package com.test.skilllet.client.bnfragments.history

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.TempFragmentBinding
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.showProgressDialog

class ClientServiceStatusFragment(var status: String) : Fragment() {
    lateinit var binding: TempFragmentBinding


    lateinit var list: ArrayList<WorkingServiceModel>
    lateinit var progressDialog: ProgressDialog

    var adapter:ClientServiceStatusAdapter?=null

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
        progressDialog = requireActivity().showProgressDialog("Please Wait", "Loading Services")

        getList()

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


    private fun setupAdapter() {
        progressDialog.dismiss()
        binding.rv.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        when (status) {
            RequestStatus.PENDING.name -> {

                adapter= ClientServiceStatusAdapter(
                    this.requireContext(),
                    list,
                    View.VISIBLE,
                    View.VISIBLE,
                    View.GONE,
                    View.GONE,
                    View.GONE
                )
                binding.rv.adapter=adapter
            }
            RequestStatus.APPROVED.name -> {
                adapter = ClientServiceStatusAdapter(
                    this.requireContext(),
                    list,
                    View.VISIBLE,
                    View.GONE,
                    View.GONE,
                    View.VISIBLE,
                    View.GONE
                )
                binding.rv.adapter=adapter
            }
            RequestStatus.COMPLETED.name -> {
                adapter = ClientServiceStatusAdapter(
                    this.requireContext(),
                    list,
                    View.GONE,
                    View.GONE,
                    View.VISIBLE,
                    View.GONE,
                    View.GONE
                )
                binding.rv.adapter=adapter
            }
            RequestStatus.DECLINE.name -> {
                adapter = ClientServiceStatusAdapter(
                    this.requireContext(),
                    list,
                    View.GONE,
                    View.VISIBLE,
                    View.GONE,
                    View.GONE,
                    View.VISIBLE

                )
                binding.rv.adapter=adapter
            }
        }


    }

    private fun getList() {
        progressDialog.show()
        Repository.getServiceByClientAndStatus(status, Repository.loggedInUser!!.key) { arrayList ->
            progressDialog.cancel()
            initList(arrayList)
        }
    }


    fun initList(arrayList: ArrayList<WorkingServiceModel>?) {
        progressDialog.cancel()
        if ((arrayList != null) && arrayList.isNotEmpty()) {
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