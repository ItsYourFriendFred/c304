package com.comp304.lab2

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class HousingRecyclerViewAdapter(
    private val itemsList: List<HousingModel>,
    private val sharedPreferences: SharedPreferences
):
    RecyclerView.Adapter<HousingRecyclerViewAdapter.MyViewHolder>() {

    private val checkedStateMap = mutableMapOf<Int, Boolean>()

    init {
        loadCheckedStates()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HousingRecyclerViewAdapter.MyViewHolder {
        // Inflate layout to give row appearance
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HousingRecyclerViewAdapter.MyViewHolder, position: Int) {
        // Assign values to rows as they come back on screen (position of recycler view)
        val currentItem: HousingModel = itemsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        // For letting the recycler view know how many items to display
        return itemsList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.listing_imageView)
        private val addressTextView: TextView = itemView.findViewById(R.id.address_textView)
        private val priceTextView: TextView = itemView.findViewById(R.id.price_textView)
        private val checkBoxView: CheckBox = itemView.findViewById(R.id.listing_checkBox)

        fun bind(housingModel: HousingModel) {
            imageView.setImageResource(housingModel.imageResourceID)
            addressTextView.text = housingModel.address
            priceTextView.text = "Price: $${"%.2f".format(housingModel.price)}"

            // Set checkbox state based on checkedStateMap
            checkBoxView.isChecked = checkedStateMap[housingModel.imageResourceID] ?: false

            // Update checkedStateMap and SharedPreferences on checkbox state change
            checkBoxView.setOnCheckedChangeListener { _, isChecked ->
                checkedStateMap[housingModel.imageResourceID] = isChecked
                saveCheckedStates()
            }
        }
    }

    private fun loadCheckedStates() {
        itemsList.forEach { model ->
            checkedStateMap[model.imageResourceID] = sharedPreferences.getBoolean(model.imageResourceID.toString(), false)
        }
    }

    private fun saveCheckedStates() {
        val editor = sharedPreferences.edit()

        val keysToRemove = mutableSetOf<String>()

        checkedStateMap.forEach { (itemId, isChecked) ->
            if (isChecked) {
                editor.putBoolean(itemId.toString(), true)
            } else {
                // If unchecked, mark for removal from SharedPreferences
                keysToRemove.add(itemId.toString())
            }
        }

        // Remove unchecked items from SharedPreferences
        keysToRemove.forEach { key ->
            editor.remove(key)
        }

        editor.apply()
    }

}