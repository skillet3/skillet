package com.test.skilllet.client.bnfragments.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.skilllet.serviceprovider.home.SPServicesFragment

class CategoriesFragmentAdapter(fragment: Fragment,var list:ArrayList<String>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =  list.size

    override fun createFragment(position: Int): Fragment {
      return  AvailableServicesTabFragment(list[position])
    }

}