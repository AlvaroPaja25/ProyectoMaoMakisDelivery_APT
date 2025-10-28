package com.example.maomakis.ui.view.main.favorito

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.maomakis.ui.view.main.favorito.fragments.*

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> DestacadoFavFragment()
            2 -> MisFavFragment()
            else -> NuevoFavFragment()
        }
    }
    override fun getItemCount(): Int {
        return 3
    }
}