package com.example.maomakis.ui.fragment.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.maomakis.databinding.FragmentFavoritoBinding

class FavoritoFragment : Fragment() {

    private var _binding: FragmentFavoritoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}