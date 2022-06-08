package com.test.skilllet.serviceprovider.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.test.skilllet.R
import com.test.skilllet.databinding.SpFragHomeBinding
import com.test.skilllet.util.OfferingStatus

class SpHomeFragment() : Fragment() {
    lateinit var binding: SpFragHomeBinding
    private lateinit var demoCollectionAdapter: SPHomeStateAdapter
    private lateinit var viewPager: ViewPager2
    var tabsList = arrayOf(OfferingStatus.OFFERED.name,OfferingStatus.REQUESTED.name,
        OfferingStatus.REJECTED.name)
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
        demoCollectionAdapter = SPHomeStateAdapter(this,tabsList)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = demoCollectionAdapter

        val tabLayout =binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsList[position]
        }.attach()

        binding.offerService.setOnClickListener {
            startActivity(Intent(this.requireContext(),AddServiceBYSP::class.java))
        }

    }
}