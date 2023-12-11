package com.example.mobliefinal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class LibraryPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter (fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        // Chọn Fragment tương ứng với vị trí
        return when (position) {
            0 -> FragmentTopic()
            1 -> FragmentFolder()
            2 -> FragmentFavorite()
            3 -> FragmentStorage()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        // Số lượng Fragment
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Tiêu đề cho từng Tab
        return when (position) {
            0 -> "Topic"
            1 -> "Folder"
            2 -> "Favorite"
            3 -> "Storage"
            else -> null
        }
    }
}