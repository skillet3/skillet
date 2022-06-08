package com.test.skilllet.serviceprovider.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class SPCustomStateAdapter(fragment: Fragment, var arr: Array<String>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = arr.size

    override fun createFragment(position: Int): Fragment {
        return SPServiceStatusFragment(arr[position])
    }
}
