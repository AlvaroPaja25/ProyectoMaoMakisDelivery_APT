package com.example.maomakis.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.domain.model.ProductListModel
import com.example.maomakis.R
class HomeVerAdapter(
    private val context: Context,
    private val list: List<ProductListModel>,
    private val listener: (ProductListModel) -> Unit
) : RecyclerView.Adapter<HomeVerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ver_img)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_vertical_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = list[position]
        holder.imageView.setImageResource(product.image)
        holder.name.text = product.name
        holder.price.text = product.price
        holder.itemView.setOnClickListener { listener(product) }
    }

    override fun getItemCount() = list.size
}
