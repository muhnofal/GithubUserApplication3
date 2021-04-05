package com.example.githubuserapplication3.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserapplication3.Fragment.FollowerFragment
import com.example.githubuserapplication3.Fragment.FollowingFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    //initialize
    var id: String? = null

    override fun createFragment(position: Int): Fragment {

        var fragment: Fragment? = null

        when(position){
            0 -> fragment =  FollowerFragment.newInstance(id)
            1 -> fragment = FollowingFragment.newInstance(id)
        }

        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}