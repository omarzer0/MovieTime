package az.zero.movietime.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import az.zero.movietime.ui.home.pager.ViewPagerFragment

class ViewPagerAdapter(fragmentManager: FragmentActivity) :
    FragmentStateAdapter(
        fragmentManager
    ) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return ViewPagerFragment.newInstance(position)
    }
}