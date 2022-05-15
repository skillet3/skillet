package com.test.skilllet.client.bnfragments.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.skilllet.util.RequestStatus


class CustomStateAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =  4

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0->{
                ClientServiceStatusFragment(RequestStatus.APPROVED)
            }
            1->{
                ClientServiceStatusFragment(RequestStatus.PENDING)
            }
            2->{
                ClientServiceStatusFragment(RequestStatus.COMPLETED)
            }
            3->{
                ClientServiceStatusFragment(RequestStatus.DECLINE)
            }
            else->{
                ClientServiceStatusFragment(RequestStatus.APPROVED)
            }
        }
    }
}