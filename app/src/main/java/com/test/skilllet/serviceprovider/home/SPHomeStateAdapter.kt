package com.test.skilllet.serviceprovider.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class SPHomeStateAdapter(fragment: Fragment, var tabsList: Array<String>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = tabsList.size

    override fun createFragment(position: Int): Fragment {
        return SPServicesFragment(tabsList[position]);
    }

}