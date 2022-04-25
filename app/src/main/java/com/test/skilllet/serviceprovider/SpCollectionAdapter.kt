package com.test.skilllet.serviceprovider

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.skilllet.serviceprovider.history.SpHistoryFragment
import com.test.skilllet.serviceprovider.home.SpHomeFragment

class SpCollectionAdapter(fragment: Fragment) :FragmentStateAdapter(fragment) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
               SpHomeFragment()
            }
            1->{
                SpHistoryFragment()
            }
            2->{
                SpRequestFragment()
            }
            3->{
                SpProfileFragment()
            }
            else->{
                SpHomeFragment()
            }
        }
    }


}