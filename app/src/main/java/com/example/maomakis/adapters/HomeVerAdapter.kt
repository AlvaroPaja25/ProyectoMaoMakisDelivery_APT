package com.example.maomakis.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.models.HomeVerModel
import com.example.maomakis.R

class HomeVerAdapter(
    private val context: Context,
    private val list: List<HomeVerModel>,
    private val listener: OnVerItemClickListener
) : RecyclerView.Adapter<HomeVerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // Componentes estándar
        val imageView: ImageView = itemView.findViewById(R.id.ver_img)
        val name: TextView = itemView.findViewById(R.id.name)
        val timing: TextView = itemView.findViewById(R.id.timing)
        val price: TextView = itemView.findViewById(R.id.price)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_vertical_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.imageView.setImageResource(item.image)
        holder.name.text = item.name
        holder.timing.text = item.timing
        holder.price.text = item.price

        //La implementacion del clicklistener
        holder.itemView.setOnClickListener {
            val clickedPosition = holder.adapterPosition

            if (clickedPosition != RecyclerView.NO_POSITION) {
                listener.onVerItemClick(list[clickedPosition])
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}