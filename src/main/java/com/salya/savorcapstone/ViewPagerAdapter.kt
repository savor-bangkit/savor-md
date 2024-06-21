package com.salya.savorcapstone

import Onboarding3Fragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.salya.savorcapstone.view.onboarding.Onboarding1Fragment
import com.salya.savorcapstone.view.onboarding.Onboarding2Fragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        Onboarding1Fragment(),
        Onboarding2Fragment(),
        Onboarding3Fragment()
    )

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

}
