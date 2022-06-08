package com.test.skilllet.client.bnfragments.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class CustomStateAdapter(fragment: Fragment, var list: Array<String>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return ClientServiceStatusFragment(list[position])
    }
}
