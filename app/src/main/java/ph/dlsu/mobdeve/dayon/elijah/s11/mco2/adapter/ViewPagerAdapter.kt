package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.os.Bundle
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments.UserFragmentRV1
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments.UserFragmentRV2
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments.UserFragmentRV3
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.User

private const val NUM_TABS = 3

public class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,
                                recentNovels:ArrayList<Novel>,
                                bookmarks:ArrayList<Novel>,
                                followedUsers: ArrayList<User>) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return UserFragmentRV1()
            1 -> return UserFragmentRV2()
        }
        return UserFragmentRV3()
    }
    }