package com.test.skilllet.client.bnfragments.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class CustomStateAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =  3

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0->{
                ClientServiceStatusFragment("Approved")
            }
            1->{
                ClientServiceStatusFragment("Requested")
            }
            2->{
                ClientServiceStatusFragment("Completed")
            }
            else->{
                ClientServiceStatusFragment("Approved")
            }
        }
    }
}