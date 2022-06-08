package com.test.skilllet.client.bnfragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.test.skilllet.R
import com.test.skilllet.databinding.HistoryFragmentBinding
import com.test.skilllet.util.RequestStatus


class HistoryFragment() : Fragment() {
    lateinit var binding: HistoryFragmentBinding
    private lateinit var demoCollectionAdapter: CustomStateAdapter
    private lateinit var viewPager: ViewPager2
    var arr = arrayOf(RequestStatus.PENDING.name,RequestStatus.APPROVED.name,
        RequestStatus.COMPLETED.name,RequestStatus.DECLINE.name)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= HistoryFragmentBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionAdapter = CustomStateAdapter(this,arr)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = demoCollectionAdapter

        val tabLayout =binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = arr[position]
        }.attach()
    }
}