package com.example.maomakis.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R

import com.example.maomakis.models.HomeHordModel
import com.example.maomakis.models.HomeVerModel

class HomeHorAdapter(
    private val updateVerticalRec: UpdateVerticalRec,
    private val activity: Activity,
    private val list: List<HomeHordModel>
) : RecyclerView.Adapter<HomeHorAdapter.ViewHolder>() {

    private var check: Boolean = true
    private var select: Boolean = true
    private var rowIndex: Int = -1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView   ) {
        val imageView: ImageView = itemView.findViewById(R.id.hor_img)
        val nameTextView: TextView = itemView.findViewById(R.id.hor_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.home_horizontal_item,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.imageView.setImageResource(currentItem.image)
        holder.nameTextView.text = currentItem.name

        if (check) {
            val initialPosition = holder.adapterPosition

            val initialList = generateVerList(currentItem.name)

            updateVerticalRec.callback(initialPosition, initialList)

            rowIndex = initialPosition
            check = false
        }

        holder.itemView.setOnClickListener {
            val clickedPosition = holder.adapterPosition

            if (clickedPosition != RecyclerView.NO_POSITION) {
                val clickedItem = list[clickedPosition]

                rowIndex = clickedPosition
                notifyDataSetChanged()

                val updatedList = generateVerList(clickedItem.name)
                updateVerticalRec.callback(clickedPosition, updatedList)
            }
        }

        if (rowIndex == position) {
            holder.itemView.setBackgroundResource(R.drawable.change_bg)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.default_bg)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun generateVerList(categoryName: String): ArrayList<HomeVerModel> {
        val newList = ArrayList<HomeVerModel>()

        when (categoryName) {
            "Pizza" -> {
                newList.add(HomeVerModel(R.drawable.pizza1, "Pizza 1", "10:00 - 23:00", "4.9", "$15.00"))
                newList.add(HomeVerModel(R.drawable.pizza2, "Pizza 2", "10:00 - 23:00", "4.9", "$18.00"))
                newList.add(HomeVerModel(R.drawable.pizza3, "Pizza 3", "10:00 - 23:00", "4.9", "$12.50"))
            }
            "Hamburger" -> {
                newList.add(HomeVerModel(R.drawable.hamburger1, "Burger 1", "09:00 - 22:00", "4.7", "$10.50"))
                newList.add(HomeVerModel(R.drawable.hamburger2, "Burger 2", "09:00 - 22:00", "4.7", "$10.50"))
                newList.add(HomeVerModel(R.drawable.hamburger3, "Burger 3", "09:00 - 22:00", "4.7", "$10.50"))
                newList.add(HomeVerModel(R.drawable.hamburger4, "Burger 4", "09:00 - 22:00", "4.7", "$10.50"))
            }
            "Fries" -> {
                newList.add(HomeVerModel(R.drawable.fried_potatoes1, "fries 1", "11:00 - 23:00", "4.0", "$5.00"))
                newList.add(HomeVerModel(R.drawable.fried_potatoes2, "fries 2", "11:00 - 23:00", "4.0", "$5.00"))
                newList.add(HomeVerModel(R.drawable.fried_potatoes3, "fries 3", "11:00 - 23:00", "4.0", "$5.00"))
                newList.add(HomeVerModel(R.drawable.fried_potatoes4, "fries 4", "11:00 - 23:00", "4.0", "$5.00"))
            }
            "Ice Cream" -> {
                newList.add(HomeVerModel(R.drawable.ice_cream1, "Ice Cream 1", "11:00 - 23:00", "4.0", "$5.00"))
                newList.add(HomeVerModel(R.drawable.ice_cream2, "Ice Cream 2", "11:00 - 23:00", "4.0", "$5.00"))
                newList.add(HomeVerModel(R.drawable.ice_cream3, "Ice Cream 3", "11:00 - 23:00", "4.0", "$5.00"))
                newList.add(HomeVerModel(R.drawable.ice_cream4, "Ice Cream 4", "11:00 - 23:00", "4.0", "$5.00"))
            }
            "Sandwich" -> {
                newList.add(HomeVerModel(R.drawable.sandwich1, "sandwich 1", "11:00 - 23:00", "4.0", "$5.00"))
                newList.add(HomeVerModel(R.drawable.sandwich2, "sandwich 2", "11:00 - 23:00", "4.0", "$5.00"))
                newList.add(HomeVerModel(R.drawable.sandwich3, "sandwich 3", "11:00 - 23:00", "4.0", "$5.00"))
                newList.add(HomeVerModel(R.drawable.sandwich4, "sandwich 3", "11:00 - 23:00", "4.0", "$5.00"))
            }

        }
        return newList
    }
}