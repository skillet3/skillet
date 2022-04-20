package com.test.skilllet.client.bnfragments.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CategoriesFragmentAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =  3

    override fun createFragment(position: Int): Fragment {
      return  when(position){
            0->{
                AvailableServicesTabFragment("Cleaning")
            }
            1->{
                AvailableServicesTabFragment("Plumbing")
            }
            2->{
                AvailableServicesTabFragment("Electrician")
            }
            else->{
                AvailableServicesTabFragment("Cleaning")
            }
        }
    }
}