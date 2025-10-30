package com.example.maomakis.ui.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.data.local.entity.Order
import com.example.maomakis.databinding.ItemOrderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdersAdapter : ListAdapter<Order, OrdersAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem
        }
    }

    inner class VH(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order) {
            binding.txtOrderId.text = "Pedido #${item.id}"
            val totalFmt = String.format(Locale.getDefault(), "S/ %.2f", item.total)
            binding.txtOrderSummary.text = "Total: $totalFmt • Pago: ${item.paymentMethod} • Envío: ${item.deliveryType}"
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.txtOrderDate.text = sdf.format(Date(item.createdAt))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
