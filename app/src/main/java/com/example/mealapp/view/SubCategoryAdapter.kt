package com.example.mealapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealapp.R
import com.example.mealapp.databinding.SubcategoryItemBinding
import com.example.mealapp.model.pojo.CategoryMeal
import com.example.mealapp.model.pojo.SubCategoryMeal

class SubCategoryAdapter(private val onItemClick:(SubCategoryMeal)->Unit,
                         private val onFavClicked: (SubCategoryMeal) -> Unit):
ListAdapter<SubCategoryMeal,SubCategoryAdapter.ViewHolder>(MyDiffUtil()) {
    class MyDiffUtil : DiffUtil.ItemCallback<SubCategoryMeal>() {
        override fun areItemsTheSame(oldItem: SubCategoryMeal, newItem: SubCategoryMeal): Boolean {
            return  oldItem.idMeal==newItem.idMeal
        }

        override fun areContentsTheSame(
            oldItem: SubCategoryMeal,
            newItem: SubCategoryMeal
        ): Boolean {
           return oldItem==newItem
        }

    }
    class ViewHolder(val binding: SubcategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SubcategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val subCategory=getItem(position)
        holder.binding.apply {
            textView27.text=subCategory.strMeal
            Glide.with(imageView28.context)
                .load(subCategory.strMealThumb)
                .into(imageView28)
            imageView29.setImageResource(
                if (subCategory.isFavorite)
                    R.drawable.fav  // ✅ your filled heart drawable
                else
                    R.drawable.fav2// ✅ your outlined heart drawable
            )
            imageView29.setOnClickListener {
                onFavClicked(subCategory)
            }

            root.setOnClickListener {
                onItemClick(subCategory)
            }
        }
    }

}