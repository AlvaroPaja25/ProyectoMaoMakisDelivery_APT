package com.example.maomakis.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.model.DetalleDiarioModel

class DetalleDiarioAdapter(
    private val context: Context,
    private val list: List<DetalleDiarioModel>
) : RecyclerView.Adapter<DetalleDiarioAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.detalle_comida_diaria_item, parent, false)
        return ViewHolder(view)
    }
    //asdasdasdasd
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.imageView.setImageResource(item.imagen)
        holder.precio.text = item.precio
        holder.nombre.text = item.nombre
        holder.descripcion.text = item.descripcion
        holder.calificacion.text = item.calificacion
        holder.tiempo.text = item.tiempo
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.detailed_img)
        val precio: TextView = itemView.findViewById(R.id.detailed_name)
        val nombre: TextView = itemView.findViewById(R.id.detailed_price)
        val descripcion: TextView = itemView.findViewById(R.id.detailed_des)
        val calificacion: TextView = itemView.findViewById(R.id.detailed_rating)
        val tiempo: TextView = itemView.findViewById(R.id.detailed_timeing)
    }
}