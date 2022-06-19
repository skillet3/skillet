package com.test.skilllet.client


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.skilllet.R
import com.test.skilllet.databinding.MainFragmentBinding


class MainFragment() : Fragment() {
    private lateinit var bnCollectionAdapter: BNCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: MainFragmentBinding
    private lateinit var bNavigation: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=MainFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bnCollectionAdapter = BNCollectionAdapter(this)
        viewPager = binding.vp
        viewPager.adapter = bnCollectionAdapter
        bNavigation=binding.bnv
        viewPager.offscreenPageLimit=3


        bNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_home -> {
                    viewPager.currentItem = 0
                    return@setOnItemSelectedListener true
                }
                R.id.mi_history -> {
                    viewPager.currentItem = 1
                    return@setOnItemSelectedListener true
                }
                R.id.mi_profile -> {
                    viewPager.currentItem = 3
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
        viewPager.isUserInputEnabled=false

//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                when (position) {
//                    0 -> bNavigation.menu.findItem(R.id.mi_home).isChecked = true
//                    1 -> bNavigation.menu.findItem(R.id.mi_history).isChecked = true
//                    2 -> bNavigation.menu.findItem(R.id.mi_notifications).isChecked = true
//                    3 -> bNavigation.menu.findItem(R.id.mi_profile).isChecked = true
//                }
//            }
//        })
    }

}