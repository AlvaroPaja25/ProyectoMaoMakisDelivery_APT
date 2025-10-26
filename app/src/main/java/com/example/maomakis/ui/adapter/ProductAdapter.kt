package com.example.maomakis.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.databinding.ItemProductBinding
import com.example.maomakis.domain.model.ProductListModel

class ProductAdapter(
    private val onAddToCartClicked: (ProductListModel) -> Unit
) : ListAdapter<ProductListModel, ProductAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductListModel) {
            binding.productName.text = item.name
            binding.productCategory.text = item.category
            binding.productDescription.text = item.description
            binding.productPrice.text = itemView.context.getString(R.string.currency_format, item.price)

            item.iconResName?.let {
                binding.productImage.setImageResource(it)
            } ?: binding.productImage.setImageResource(R.drawable.ic_launcher_foreground) // Imagen por defecto

            binding.buttonAddToCart.setOnClickListener {
                onAddToCartClicked(item)
            }
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
