package com.example.mealapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealapp.databinding.CategoryDetailsItemBinding
import com.example.mealapp.model.pojo.IngredientItem

class CategoryDetailsAdapter(private val onItemClick: (IngredientItem) -> Unit) :
    ListAdapter<IngredientItem, CategoryDetailsAdapter.ViewHolder>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<IngredientItem>() {
        override fun areItemsTheSame(oldItem: IngredientItem, newItem: IngredientItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: IngredientItem, newItem: IngredientItem): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val binding: CategoryDetailsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)  // ✅ استخدم getItem بدلًا من items[position]

        // ✅ استخدم binding للوصول إلى العناصر داخل XML
        holder.binding.ingredientName.text = item.name
        holder.binding.ingredientMeasure.text = item.measure
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.binding.ingredientImage)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }


}

