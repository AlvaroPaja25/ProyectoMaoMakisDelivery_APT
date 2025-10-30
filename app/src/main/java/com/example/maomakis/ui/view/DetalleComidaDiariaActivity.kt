package com.example.maomakis.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.domain.modelss.DetalleDiarioModel
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.view.adapter.DetalleDiarioAdapter
import com.example.maomakis.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class DetalleComidaDiariaActivity : AppCompatActivity() {
    //asdasdasd
    private val productViewModel: ProductViewModel by viewModels {
        ViewModelFactory(application, this)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var detalleDiarioModelList: MutableList<DetalleDiarioModel>
    private lateinit var diarioAdapter: DetalleDiarioAdapter
    private lateinit var imageView: ImageView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_comida_diaria)

        val type = intent.getStringExtra("type")?.lowercase()
        recyclerView = findViewById(R.id.detailed_rec)
        imageView = findViewById(R.id.detailed_img)

        recyclerView.layoutManager = LinearLayoutManager(this)
        detalleDiarioModelList = ArrayList()
        diarioAdapter = DetalleDiarioAdapter(this, detalleDiarioModelList)
        recyclerView.adapter = diarioAdapter

        val tipoPlato = when (type) {
            "desayuno" -> 1
            "almuerzo" -> 2
            "cena" -> 3
            "dulces" -> 4
            "café" -> 5
            else -> null
        }

        val imageRes = when(type) {
            "desayuno" -> R.drawable.breakfast
            "almuerzo" -> R.drawable.lunch
            "cena" -> R.drawable.dinner
            "dulces" -> R.drawable.sweets
            "café" -> R.drawable.coffe
            else -> 0
        }
        if (imageRes != 0) {
            imageView.setImageResource(imageRes)
        }

        tipoPlato?.let { tipo ->
            lifecycleScope.launch {
                val productos = productViewModel.getProductsByTipoPlato(tipo)
                val mapped = productos.map {
                    DetalleDiarioModel(
                        imagen = it.iconResName
                            ?: R.drawable.ic_launcher_foreground, // Usa un ícono por defecto si es null
                        nombre = it.name,
                        descripcion = it.description ?: "Sin descripción",
                        calificacion = it.rating.toString(),
                        precio = it.price.toString(),
                        tiempo = "10 a 9" // Puedes adaptar esto si tienes un campo real
                    )
                }
                detalleDiarioModelList.clear()
                detalleDiarioModelList.addAll(mapped)
                diarioAdapter.notifyDataSetChanged()
            }
        }
    }
}