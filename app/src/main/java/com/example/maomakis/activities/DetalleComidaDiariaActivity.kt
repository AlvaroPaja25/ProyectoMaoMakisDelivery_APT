package com.example.maomakis.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.adapters.DetalleDiarioAdapter
import com.example.maomakis.model.DetalleDiarioModel

class DetalleComidaDiariaActivity : AppCompatActivity() {
    //asdasdasd
    private lateinit var recyclerView: RecyclerView
    private lateinit var detalleDiarioModelList: MutableList<DetalleDiarioModel>
    private lateinit var diarioAdapter: DetalleDiarioAdapter
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_comida_diaria)

        val type = intent.getStringExtra("type")

        recyclerView = findViewById(R.id.detailed_rec)
        imageView = findViewById(R.id.detailed_img)

        recyclerView.layoutManager = LinearLayoutManager(this)
        detalleDiarioModelList = ArrayList()
        diarioAdapter = DetalleDiarioAdapter(this, detalleDiarioModelList)
        recyclerView.adapter = diarioAdapter

        when (type?.lowercase()) {
            "desayuno" -> {
                detalleDiarioModelList.add(
                    DetalleDiarioModel(
                        R.drawable.fav1,
                        "Desayuno",
                        "Descripción",
                        "4.4",
                        "40",
                        "10 a 9"
                    ))
                detalleDiarioModelList.add(
                    DetalleDiarioModel(
                        R.drawable.fav2,
                        "Desayuno",
                        "Descripción",
                        "4.4",
                        "40",
                        "10 a 9"
                    ))
                detalleDiarioModelList.add(
                    DetalleDiarioModel(
                        R.drawable.fav3,
                        "Desayuno",
                        "Descripción",
                        "4.4",
                        "40",
                        "10 a 9"
                    ))
                diarioAdapter.notifyDataSetChanged()
            }
        }

        when (type?.lowercase()) {
            "dulces" -> {
                imageView.setImageResource(R.drawable.sweets)

                detalleDiarioModelList.add(
                    DetalleDiarioModel(
                        R.drawable.s1,
                        "Dulces",
                        "Descripción",
                        "4.4",
                        "40",
                        "10 a 9"
                    ))
                detalleDiarioModelList.add(
                    DetalleDiarioModel(
                        R.drawable.s2,
                        "Dulces",
                        "Descripción",
                        "4.4",
                        "40",
                        "10 a 9"
                    ))
                detalleDiarioModelList.add(
                    DetalleDiarioModel(
                        R.drawable.s3,
                        "Dulces",
                        "Descripción",
                        "4.4",
                        "40",
                        "10 a 9"
                    ))
                diarioAdapter.notifyDataSetChanged()
            }
        }
    }
}