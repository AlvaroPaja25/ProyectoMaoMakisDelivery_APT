package com.example.maomakis.ui.fragment.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.adapters.ComidaDiariaAdapter
import com.example.maomakis.model.ComidaDiariaModel

class ComidaDiariaFragment : Fragment() {
    //Aasdasdasdasd
    private lateinit var recyclerView: RecyclerView
    private lateinit var comidaDiariaModels: MutableList<ComidaDiariaModel>
    private lateinit var comidaDiariaAdapter: ComidaDiariaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.comida_diaria_fragment, container, false)

        recyclerView = root.findViewById(R.id.comida_diaria_rec)
        recyclerView.layoutManager = LinearLayoutManager(context)

        comidaDiariaModels = ArrayList()

        comidaDiariaModels.add(ComidaDiariaModel(R.drawable.breakfast, "Desayuno", "30% OFF", "Descripción Descripción","desayuno"))
        comidaDiariaModels.add(ComidaDiariaModel(R.drawable.lunch, "Almuerzo", "15% OFF", "Descripción Descripción","almuerzo"))
        comidaDiariaModels.add(ComidaDiariaModel(R.drawable.dinner, "Cena", "25% OFF", "Descripción Descripción","cena"))
        comidaDiariaModels.add(ComidaDiariaModel(R.drawable.sweets, "Dulces", "35% OFF", "Descripción Descripción","dulces"))
        comidaDiariaModels.add(ComidaDiariaModel(R.drawable.coffe, "Café", "30% OFF", "Descripción Descripción","café"))


        comidaDiariaAdapter = ComidaDiariaAdapter(requireContext(), comidaDiariaModels)
        recyclerView.adapter = comidaDiariaAdapter
        comidaDiariaAdapter.notifyDataSetChanged()

        return root
    }
}