package com.example.maomakis.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.databinding.HomeVerticalItemBinding
import com.example.maomakis.domain.model.ProductListModel

class ProductAdapter(
    private val onAddToCartClicked: (ProductListModel) -> Unit,
    private val onItemClicked: (ProductListModel) -> Unit
) : ListAdapter<ProductListModel, ProductAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeVerticalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: HomeVerticalItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductListModel) {
            binding.name.text = item.name
            binding.rating.text = item.score
            binding.timing.text = itemView.context.getString(R.string._10_00_23_00)
            binding.price.text = itemView.context.getString(R.string.currency_format, item.price)

            item.iconResName?.let {
                binding.verImg.setImageResource(it)
            } ?: binding.verImg.setImageResource(R.drawable.ic_launcher_foreground)

            // Click en tarjeta para ver detalle (imagen incluida, no agregar al carrito aquí)
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ProductListModel>() {
        override fun areItemsTheSame(oldItem: ProductListModel, newItem: ProductListModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductListModel, newItem: ProductListModel): Boolean {
            return oldItem == newItem
        }
    }
}
