package com.test.skilllet.client.bnfragments.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.skilllet.util.RequestStatus


class CustomStateAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =  3

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0->{
                ClientServiceStatusFragment(RequestStatus.APPROVED.name)
            }
            1->{
                ClientServiceStatusFragment(RequestStatus.PENDING.name)
            }
            2->{
                ClientServiceStatusFragment(RequestStatus.COMPLETED.name)
            }
            else->{
                ClientServiceStatusFragment(RequestStatus.APPROVED.name)
            }
        }
    }
}