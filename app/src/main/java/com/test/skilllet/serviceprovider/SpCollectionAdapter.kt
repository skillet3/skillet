package com.test.skilllet.serviceprovider

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.skilllet.serviceprovider.history.SpHistoryFragment
import com.test.skilllet.serviceprovider.home.SpHomeFragment
import com.test.skilllet.serviceprovider.profile.SpProfileFragment
import com.test.skilllet.serviceprovider.requests.SpRequestFragment

class SpCollectionAdapter(fragment: Fragment) :FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
               SpHomeFragment()
            }
            1->{
                SpHistoryFragment()
            }
            2->{
                SpProfileFragment()
            }
            else->{
                SpHomeFragment()
            }
        }
    }


}