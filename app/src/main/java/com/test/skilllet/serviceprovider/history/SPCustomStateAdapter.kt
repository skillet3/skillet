package com.test.skilllet.serviceprovider.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.skilllet.util.RequestStatus


class SPCustomStateAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =  2

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0->{
                SPServiceStatusFragment(RequestStatus.APPROVED)
            }
            1->{
                SPServiceStatusFragment(RequestStatus.COMPLETED)
            }
            else->{
                SPServiceStatusFragment(RequestStatus.APPROVED)
            }
        }
    }
}