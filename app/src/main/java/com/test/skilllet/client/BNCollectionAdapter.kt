package com.test.skilllet.client

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.skilllet.client.bnfragments.history.HistoryFragment
import com.test.skilllet.client.bnfragments.home.HomeFragment
import com.test.skilllet.client.bnfragments.notification.NotificationFragment
import com.test.skilllet.client.bnfragments.profile.ProfileFragment

class BNCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 ->{
                HomeFragment()
            }
            1->{
                HistoryFragment()
            }
            2->{
                ProfileFragment()
            }
            else->{
                HomeFragment()
            }
        }

    }
}
