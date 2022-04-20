package com.test.skilllet.client.bnfragments.history

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

class ClientServiceStatusFragment (var title: String): Fragment() {
    lateinit var binding: TempFragmentBinding
    var arr = arrayOf("Approved", "Pending", "Completed")
    var services = arrayOf("Cleaning", "Plumbing", "Electrician")
    var serviceIcons =
        arrayOf(R.drawable.ic_cleaning, R.drawable.ic_plumbing, R.drawable.ic_electrician)
    lateinit var list: ArrayList<ServiceModel>
    var listIcons = ArrayList<Drawable>()
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
        populateAdapter()

    }

    private fun populateAdapter() {
       initList()
        when (title) {
            arr[0] -> {
                var adapter = activity?.resources?.getColor(R.color.approved)
                    ?.let { ClientServiceStatusAdapter(list, listIcons, it) }
                binding.rv.adapter = adapter
            }
            arr[1] -> {
                var adapter = activity?.resources?.getColor(R.color.requested)
                    ?.let { ClientServiceStatusAdapter(list, listIcons, it) }
                binding.rv.adapter = adapter
                binding.rv.adapter = adapter
            }
            arr[2] -> {
                var adapter = activity?.resources?.getColor(R.color.completed)
                    ?.let { ClientServiceStatusAdapter(list, listIcons, it) }
                binding.rv.adapter = adapter
                binding.rv.adapter = adapter
            }
        }
    }

    private fun initList() {
        when (title) {
            "Approved" -> {
                Repository.getClientApprovedServices { arrayList ->
                    list= ArrayList(arrayList)
                }
            }
            "Pending" -> {
                Repository.getClientPendingServices {
                    list=ArrayList(it)
                }
            }
            "Completed" -> {
                Repository.getClientCompletedServices {
                    list=ArrayList(it)
                }
            }
            else -> {
                Repository.getClientApprovedServices {

                }
            }
        }
    }

}