package com.test.skilllet.client.bnfragments.home


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
import com.test.skilllet.util.showProgressDialog


class AvailableServicesTabFragment(var title: String):Fragment() {
    lateinit var binding: TempFragmentBinding
     var list:ArrayList<WorkingServiceModel>?=null

    var adapter:AvailableServicesAdapter?=null

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
             adapter= AvailableServicesAdapter(list!!,this.requireContext())
            binding.rv.adapter=adapter
            binding.ivEmpty.visibility=View.GONE
            binding.rv.visibility=View.VISIBLE
        }else{
            binding.ivEmpty.visibility=View.VISIBLE
            binding.rv.visibility=View.GONE
        }
    }

}