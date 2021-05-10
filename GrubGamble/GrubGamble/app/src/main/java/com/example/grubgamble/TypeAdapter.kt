/*package com.example.grubgamble

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TypeAdapter (
    private val fTypes: MutableList<FoodType>
) : RecyclerView.Adapter<TypeAdapter.FTypeViewHolder>() {
    class FTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FTypeViewHolder {
            return FTypeViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_food,
                    parent,
                    false

                )
            )
        }

        override fun getItemCount(): Int {
            return fTypes.size
        }

        fun addFType(foodtype: FoodType ) {
            fTypes.add(foodtype)
            notifyItemInserted(fTypes.size - 1)
        }

        fun deleteFType() {
            fTypes.removeAll { foodtype ->
                foodtype.isChecked
            }
            notifyDataSetChanged()
        }

        private fun toggleStrikeThrough(tvFoodTitle: TextView, isChecked: Boolean) {
            if(isChecked) {
                tvFoodTitle.paintFlags = tvFoodTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
            } else {
                tvFoodTitle.paintFlags = tvFoodTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        override fun onBindViewHolder(holder: FTypeViewHolder, position: Int) {

            val curFType = fTypes[position]
            holder.itemView.apply {
                val tvFoodTitle = findViewById<TextView>(R.id.tvFoodTitle)
                val cbDone = findViewById<CheckBox>(R.id.cbDone)
                tvFoodTitle.text = curFType.title
                cbDone.isChecked = curFType.isChecked
                toggleStrikeThrough(tvFoodTitle, curFType.isChecked)
                cbDone.setOnCheckedChangeListener { _ , isChecked ->
                    toggleStrikeThrough(tvFoodTitle, isChecked)
                    curFType.isChecked = !curFType.isChecked
                }
            }

            var choices: ArrayList<String> = ArrayList()

            //adding cuisines to a list and ignoring duplicates
            for(i in 0 until fTypes.size ){
                if(!choices.contains(fTypes[i].title.toLowerCase())) {
                    choices.add(fTypes[i].title.toLowerCase())
                }
            }
            //Log.d("tag", "onBindViewHolder: $choices")

        }


    }

*/