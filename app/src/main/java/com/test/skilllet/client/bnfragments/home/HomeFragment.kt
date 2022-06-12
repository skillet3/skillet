package com.test.skilllet.client.bnfragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.HomeFragmentBinding

class HomeFragment() : Fragment() {
    lateinit var binding: HomeFragmentBinding

    private lateinit var demoCollectionAdapter: CategoriesFragmentAdapter
    private lateinit var viewPager: ViewPager2

    var categories= ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= HomeFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Repository.getListOfServiceTypes (){
            if (it != null) {
                categories=it
                demoCollectionAdapter = CategoriesFragmentAdapter(this,categories)
                viewPager = binding.pager
                viewPager.adapter = demoCollectionAdapter
                val tabLayout =binding.tabLayout
                        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                            tab.text = categories[position]
                        }.attach()

            }
        }
        //

    }
}