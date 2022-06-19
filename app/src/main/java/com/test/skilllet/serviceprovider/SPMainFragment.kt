package com.test.skilllet.serviceprovider

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.skilllet.R
import com.test.skilllet.databinding.SpMainFragmentBinding

class SPMainFragment(): Fragment() {
    lateinit var binding: SpMainFragmentBinding

    lateinit var viewPager:ViewPager2
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var spCollectionAdapter: SpCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SpMainFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spCollectionAdapter=SpCollectionAdapter(this)
        viewPager=binding.spVp
        bottomNavigationView=binding.spBnv
        viewPager.adapter=spCollectionAdapter
        viewPager.offscreenPageLimit=3
        bottomNavigationView.setOnItemSelectedListener { menuItem: MenuItem ->
            when(menuItem.itemId){
                R.id.mi_sp_home->{
                    viewPager.currentItem=0
                    return@setOnItemSelectedListener true
                }
                R.id.mi_sp_history->{
                    viewPager.currentItem=1
                    return@setOnItemSelectedListener true

                }
                R.id.mi_sp_profile->{
                    viewPager.currentItem=3
                    return@setOnItemSelectedListener true
                }
                else->{
                    viewPager.currentItem=0
                    return@setOnItemSelectedListener true
                }
            }
        }

        viewPager.isUserInputEnabled=false


    }
}