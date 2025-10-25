package com.example.maomakis.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.databinding.MicarritoItemBinding
import com.example.maomakis.domain.model.CarritoModel

class MiCarritoAdapter(
    private val onIncreaseClicked: (CarritoModel) -> Unit,
    private val onDecreaseClicked: (CarritoModel) -> Unit
) : ListAdapter<CarritoModel, MiCarritoAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MicarritoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: MicarritoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarritoModel) {
            binding.itemName.text = item.name
            binding.itemQuantity.text = item.cant.toString()
            binding.itemPrice.text = itemView.context.getString(R.string.currency_format, item.price)
            binding.itemScore.text = item.score
            binding.itemSubTotal.text = itemView.context.getString(R.string.currency_format, item.subTotal)

            item.iconResName?.let {
                binding.itemImage.setImageResource(it)
            } ?: binding.itemImage.setImageResource(R.drawable.ic_menu_camera) // Imagen por defecto

            binding.buttonIncrease.setOnClickListener {
                onIncreaseClicked(item)
            }

            binding.buttonDecrease.setOnClickListener {
                onDecreaseClicked(item)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CarritoModel>() {
        override fun areItemsTheSame(oldItem: CarritoModel, newItem: CarritoModel): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CarritoModel, newItem: CarritoModel): Boolean {
            return oldItem == newItem
        }
    }
}