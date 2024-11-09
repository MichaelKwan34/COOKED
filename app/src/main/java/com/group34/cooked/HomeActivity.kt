package com.group34.cooked

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.group34.cooked.fragments.BrowseFragment
import com.group34.cooked.fragments.HomeFragment
import com.group34.cooked.fragments.ProfileFragment
import com.group34.cooked.fragments.RecipesFragment

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val vPager2 = findViewById<ViewPager2>(R.id.viewpager)
        val tabLayout = findViewById<TabLayout>(R.id.tab)

        val fragments = ArrayList<Fragment>().apply {
            add(HomeFragment())
            add(RecipesFragment())
            add(BrowseFragment())
            add(ProfileFragment())
        }

        val fragmentStateAdapter = MyFragmentStateAdapter(this, fragments)
        vPager2.adapter = fragmentStateAdapter

        val tabTitles = arrayOf("","","","")

        val tabConfig = TabConfigurationStrategy { tab, position ->
            tab.text = tabTitles[position]
        }

        val tabLayoutMediator = TabLayoutMediator(tabLayout, vPager2, tabConfig)
        tabLayoutMediator.attach()
    }
}