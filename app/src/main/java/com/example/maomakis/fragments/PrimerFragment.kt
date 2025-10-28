package com.example.maomakis.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.adapters.DestacadoAdapter
import com.example.maomakis.adapters.DestacadoVerAdapter
import com.example.maomakis.models.DestacadoModel
import com.example.maomakis.models.DestacadoVerModel

class PrimerFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var destacadoAdapter: DestacadoAdapter
    private lateinit var destacadoModelsList: MutableList<DestacadoModel>


    private lateinit var recyclerView2: RecyclerView
    private lateinit var destacadoVerAdapter: DestacadoVerAdapter
    private lateinit var destacadoVerModelsList: MutableList<DestacadoVerModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_primer, container, false)

        //hor
        recyclerView = view.findViewById(R.id.destacado_hor_rec)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)

        destacadoModelsList = mutableListOf(
            DestacadoModel(R.drawable.fav1, "Destacado 1", "Descripcion 1"),
            DestacadoModel(R.drawable.fav2, "Destacado 2", "Descripcion 2"),
            DestacadoModel(R.drawable.fav3, "Destacado 3", "Descripcion 3")
        )

        destacadoAdapter = DestacadoAdapter(destacadoModelsList)
        recyclerView.adapter = destacadoAdapter

        //ver
        recyclerView2 = view.findViewById(R.id.destacado_ver_rec)
        recyclerView2.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)

        destacadoVerModelsList = mutableListOf(
            DestacadoVerModel(
                R.drawable.ver1,
                "Destacado 1",
                "Descripción 1",
                "4.8",
                "10:00 - 20:00"
            ),
            DestacadoVerModel(
                R.drawable.ver2,
                "Destacado 2",
                "Descripción 2",
                "4.6",
                "09:30 - 19:00"
            ),
            DestacadoVerModel(
                R.drawable.ver3,
                "Destacado 3",
                "Descripción 3",
                "4.9",
                "11:00 - 21:00"
            ),
            DestacadoVerModel(
                R.drawable.ver2,
                "Destacado 2",
                "Descripción 2",
                "4.6",
                "09:30 - 19:00"
            ),
            DestacadoVerModel(
                R.drawable.ver3,
                "Destacado 3",
                "Descripción 3",
                "4.9",
                "11:00 - 21:00"
            ),
            DestacadoVerModel(
                R.drawable.ver2,
                "Destacado 2",
                "Descripción 2",
                "4.6",
                "09:30 - 19:00"
            ),
            DestacadoVerModel(
                R.drawable.ver3,
                "Destacado 3",
                "Descripción 3",
                "4.9",
                "11:00 - 21:00"
            )
        )

        destacadoVerAdapter = DestacadoVerAdapter(destacadoVerModelsList)
        recyclerView2.adapter = destacadoVerAdapter

        return view
    }
}