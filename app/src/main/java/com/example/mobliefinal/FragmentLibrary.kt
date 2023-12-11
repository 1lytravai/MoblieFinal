package com.example.mobliefinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class FragmentLibrary : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayoutLibrary)
        val viewPager: ViewPager = view.findViewById(R.id.viewPagerLibrary)

        // Tạo đối tượng Adapter cho ViewPager và đặt nó vào ViewPager
        val pagerAdapter = LibraryPagerAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter

        // Đặt ViewPager cho TabLayout
        tabLayout.setupWithViewPager(viewPager)

        return view
    }
}


