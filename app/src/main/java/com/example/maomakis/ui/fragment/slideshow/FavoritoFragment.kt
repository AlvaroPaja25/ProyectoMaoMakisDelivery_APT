package com.example.maomakis.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.models.HomeHordModel
import com.example.maomakis.adapters.HomeHorAdapter
import com.example.maomakis.R
import com.example.maomakis.adapters.HomeVerAdapter
import com.example.maomakis.adapters.UpdateVerticalRec
import com.example.maomakis.adapters.OnVerItemClickListener
import com.example.maomakis.models.HomeVerModel
import java.util.ArrayList

import android.content.Intent
import com.example.maomakis.ui.detail.ProductDetailActivity
// ¡CAMBIO CLAVE! Agrega el import para tu Bottom Sheet
import com.example.maomakis.ui.detail.ProductDetailBottomSheet
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), UpdateVerticalRec, OnVerItemClickListener {

    //... (Variables y onCreateView)

    //HomeHorizontal
    private lateinit var homeHorizontalRec: RecyclerView
    private lateinit var homeHorModelList: MutableList<HomeHordModel>
    private lateinit var homeHorAdapter: HomeHorAdapter


    //HomeVertical
    private lateinit var homeVerticalRec: RecyclerView
    private lateinit var homeVerModelList: MutableList<HomeVerModel>
    private lateinit var homeVerAdapter: HomeVerAdapter

    private lateinit var greetingTextView: TextView
    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        greetingTextView = root.findViewById(R.id.textView7)
        homeHorizontalRec = root.findViewById(R.id.home_hor_rec)
        homeHorModelList = ArrayList<HomeHordModel>()
        homeHorModelList.add(HomeHordModel(R.drawable.pizza, "Pizza"))
        homeHorModelList.add(HomeHordModel(R.drawable.hamburger, "Hamburger"))
        homeHorModelList.add(HomeHordModel(R.drawable.fried_potatoes, "Fries"))
        homeHorModelList.add(HomeHordModel(R.drawable.ice_cream, "Ice Cream"))
        homeHorModelList.add(HomeHordModel(R.drawable.sandwich, "Sandwich"))

        homeHorAdapter = HomeHorAdapter(this, requireActivity(), homeHorModelList)
        homeHorizontalRec.adapter = homeHorAdapter

        homeHorizontalRec.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )
        homeHorizontalRec.setHasFixedSize(true)
        homeHorizontalRec.isNestedScrollingEnabled = false


        homeVerticalRec = root.findViewById(R.id.home_ver_rec)
        homeVerModelList = ArrayList<HomeVerModel>()

        homeVerAdapter = HomeVerAdapter(requireContext(), homeVerModelList, this)
        homeVerticalRec.adapter = homeVerAdapter

        homeVerticalRec.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        homeVerticalRec.setHasFixedSize(true)
        homeVerticalRec.isNestedScrollingEnabled = false

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.loggedInUser.collect { user ->
                val greeting = if (user != null && user.displayName.isNotBlank()) {
                    "Hola ${user.displayName}"
                } else {
                    getString(R.string.hola)
                }
                greetingTextView.text = greeting
            }
        }
    }

    override fun callback(
        position: Int,
        list: ArrayList<HomeVerModel>
    ) {
        homeVerModelList.clear()
        homeVerModelList.addAll(list)
        homeVerAdapter.notifyDataSetChanged()
    }

    override fun onVerItemClick(item: HomeVerModel) {

        val detailFragment = ProductDetailBottomSheet.newInstance(
            item.name,
            item.price,
            item.image
        )

        detailFragment.show(childFragmentManager, detailFragment.tag)
    }
}