package com.example.mealapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealapp.R
import com.example.mealapp.databinding.MainCategoryItemBinding
import com.example.mealapp.model.pojo.CategoryMeal

class HomeAdapter(private val onItemClick: (CategoryMeal) -> Unit,
                  private val onFavClicked: (CategoryMeal) -> Unit)
    :
    ListAdapter<CategoryMeal, HomeAdapter.ViewHolder>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<CategoryMeal>() {
        override fun areItemsTheSame(oldItem: CategoryMeal, newItem: CategoryMeal): Boolean {
            return oldItem.idCategory == newItem.idCategory
        }

        override fun areContentsTheSame(oldItem: CategoryMeal, newItem: CategoryMeal): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val binding: MainCategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MainCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category = getItem(position)
        holder.binding.apply {
            textView18.text = category.strCategory
//            textView19.text = category.strCategoryDescription
            Glide.with(imageView19.context)
                .load(category.strCategoryThumb)
                .into(imageView19)
            imageView18.setImageResource(
                if (category.isFavorite)
                    R.drawable.fav  // ✅ your filled heart drawable
                else
                    R.drawable.fav2// ✅ your outlined heart drawable
            )
            imageView18.setOnClickListener {
                onFavClicked(category)
            }
            root.setOnClickListener {
                onItemClick(category)
            }
        }
    }
}
