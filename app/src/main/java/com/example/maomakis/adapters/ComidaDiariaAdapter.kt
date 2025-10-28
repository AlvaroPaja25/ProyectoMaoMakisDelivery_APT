package com.example.maomakis.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.model.ComidaDiariaModel
import com.example.maomakis.activities.DetalleComidaDiariaActivity

class ComidaDiariaAdapter(
    private val context: Context,
    private val list: List<ComidaDiariaModel>
) : RecyclerView.Adapter<ComidaDiariaAdapter.ViewHolder>() {
    //asdasdasdasdasd
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comida_diaria_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.imageView.setImageResource(item.imagen)
        holder.nombre.text = item.nombre
        holder.descripcion.text = item.descripcion
        holder.descuento.text = item.descuento

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetalleComidaDiariaActivity::class.java)
            intent.putExtra("type", list[position].tipo)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val nombre: TextView = itemView.findViewById(R.id.textView3)
        val descripcion: TextView = itemView.findViewById(R.id.textView7)
        val descuento: TextView = itemView.findViewById(R.id.discount)
    }
}