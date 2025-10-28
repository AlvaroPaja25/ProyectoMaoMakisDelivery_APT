package com.example.maomakis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.models.DestacadoVerModel
import com.example.maomakis.R


class DestacadoVerAdapter (private val list: List<DestacadoVerModel>) :
    RecyclerView.Adapter<DestacadoVerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.destacado_ver_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.imageView.setImageResource(item.image)
        holder.name.text = item.name
        holder.description.text = item.desc
        holder.timing.text = item.timing
        holder.rating.text = item.rating
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.detalle_img)
        val name: TextView = itemView.findViewById(R.id.detalle_name)
        val description: TextView = itemView.findViewById(R.id.detalle_des)
        val rating: TextView = itemView.findViewById(R.id.detalle_rating)
        val timing: TextView = itemView.findViewById(R.id.detalle_timing)
    }
}