package com.example.maomakis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.adapters.CarritoAdapter
import com.example.maomakis.models.CarritoModel
import java.text.NumberFormat
import java.util.Locale

class MiCarritoFragment : Fragment() {

    private lateinit var carritoAdapter: CarritoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalAmountTextView: TextView
    private val carritoItems = mutableListOf<CarritoModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mi_carrito, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.cart_items_recycler_view)
        totalAmountTextView = view.findViewById(R.id.total_amount)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Datos de ejemplo actualizados
        carritoItems.add(CarritoModel(R.drawable.ic_menu_camera, "Sushi Roll", 180.0, "5.0"))
        carritoItems.add(CarritoModel(R.drawable.ic_menu_camera, "Ramen", 250.0, "4.8"))
        carritoItems.add(CarritoModel(R.drawable.ic_menu_camera, "Gyoza", 120.0, "4.9"))

        carritoAdapter = CarritoAdapter(carritoItems)
        recyclerView.adapter = carritoAdapter

        calculateTotal()
    }

    private fun calculateTotal() {
        val total = carritoItems.sumOf { it.price }
        val format = NumberFormat.getCurrencyInstance(Locale.US)
        totalAmountTextView.text = format.format(total)
    }
}
