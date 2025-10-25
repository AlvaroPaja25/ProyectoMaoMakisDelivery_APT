package com.example.maomakis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.maomakis.R
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.models.DestacadoModel

class DestacadoAdapter(
    private val list: List<DestacadoModel>
) : RecyclerView.Adapter<DestacadoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.destacado_hor_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.image.setImageResource(item.image)
        holder.name.text = item.name
        holder.desc.text = item.desc
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.destacado_img)
        val name: TextView = itemView.findViewById(R.id.destacado_name)
        val desc: TextView = itemView.findViewById(R.id.destacado_des)
    }
}
