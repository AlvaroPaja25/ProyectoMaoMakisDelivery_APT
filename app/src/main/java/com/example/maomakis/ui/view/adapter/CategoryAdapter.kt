package com.example.maomakis.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.databinding.HomeHorizontalItemBinding
import com.example.maomakis.domain.model.CategoryListModel
import com.example.maomakis.R

class CategoryAdapter(
    private val onCategoryClicked: (CategoryListModel) -> Unit
) : ListAdapter<CategoryListModel, CategoryAdapter.ViewHolder>(DiffCallback) {

    private var selectedCategoryId: Int? = null

    fun setSelectedCategoryId(categoryId: Int?) {
        val previousIndex = currentList.indexOfFirst { it.id == selectedCategoryId }
        selectedCategoryId = categoryId
        val newIndex = currentList.indexOfFirst { it.id == selectedCategoryId }
        if (previousIndex >= 0) notifyItemChanged(previousIndex)
        if (newIndex >= 0) notifyItemChanged(newIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeHorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: HomeHorizontalItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryListModel) {
            binding.horText.text = item.name
            item.iconResName?.let {
                binding.horImg.setImageResource(it)
            } ?: binding.horImg.setImageResource(R.drawable.ic_launcher_foreground)

            val isSelected = item.id == selectedCategoryId
            val ctx = binding.root.context
            val selectedColor = ctx.getColor(R.color.red)
            val defaultColor = ctx.getColor(android.R.color.transparent)
            binding.cardView.setCardBackgroundColor(if (isSelected) selectedColor else defaultColor)

            binding.root.setOnClickListener {
                val previousIndex = currentList.indexOfFirst { it.id == selectedCategoryId }
                selectedCategoryId = item.id
                val newIndex = adapterPosition
                if (previousIndex >= 0) notifyItemChanged(previousIndex)
                if (newIndex >= 0) notifyItemChanged(newIndex)
                onCategoryClicked(item)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CategoryListModel>() {
        override fun areItemsTheSame(oldItem: CategoryListModel, newItem: CategoryListModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryListModel, newItem: CategoryListModel): Boolean {
            return oldItem == newItem
        }
    }
}
