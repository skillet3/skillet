package com.test.skilllet.serviceprovider.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.skilllet.util.RequestStatus


class SPCustomStateAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =  3

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0->{
                SPServiceStatusFragment(RequestStatus.APPROVED)
            }
            1->{
                SPServiceStatusFragment(RequestStatus.COMPLETED)
            }
            2->{
                SPServiceStatusFragment(RequestStatus.DECLINE)
            }
            else->{
                SPServiceStatusFragment(RequestStatus.APPROVED)
            }
        }
    }
}