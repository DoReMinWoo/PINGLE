package org.sopt.pingle.presentation.ui.plan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PlanFragmentStateAdapter(
    private val fragmentList: ArrayList<Fragment>,
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
