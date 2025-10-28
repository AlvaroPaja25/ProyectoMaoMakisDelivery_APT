package com.example.maomakis.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.maomakis.domain.model.ProductListModel

/**
 * Un adaptador genérico y reutilizable para listas de productos que utiliza ViewBinding.
 *
 * @param VB El tipo de ViewBinding para el layout del item.
 * @param bindingInflater Una función lambda para inflar el ViewBinding. Ej: HomeVerticalItemBinding::inflate
 * @param binder Una función lambda que define cómo enlazar un ProductListModel a las vistas y
 *               establecer los click listeners. Recibe el binding y el item.
 */
class ProductoAdapter<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val binder: (binding: VB, item: ProductListModel) -> Unit
) : ListAdapter<ProductListModel, ProductoAdapter.ViewHolder<VB>>(DiffCallback) {

    class ViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = bindingInflater(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        val item = getItem(position)
        // El binder se encarga de todo: enlazar datos y listeners.
        binder(holder.binding, item)
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
