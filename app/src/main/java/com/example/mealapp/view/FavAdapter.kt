package com.example.mealapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealapp.R
import com.example.mealapp.databinding.SubcategoryItemBinding
import com.example.mealapp.model.pojo.MealDataFav
import com.example.mealapp.model.pojo.SubCategoryMeal
import com.example.mealapp.view.SubCategoryAdapter.ViewHolder

class FavAdapter(private val onFavClicked:(MealDataFav)->Unit):
    ListAdapter<MealDataFav, FavAdapter.ViewHolder>(MyDiffUtil()) {
    class MyDiffUtil : DiffUtil.ItemCallback<MealDataFav>() {
        override fun areItemsTheSame(oldItem: MealDataFav, newItem: MealDataFav): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MealDataFav,
            newItem: MealDataFav
        ): Boolean {
            return oldItem == newItem
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
            textView27.text=subCategory.title
            Glide.with(imageView28.context)
                .load(subCategory.img)
                .into(imageView28)
            imageView29.setImageResource( R.drawable.fav)
            imageView29.setOnClickListener {
                // Change icon to empty immediately
                imageView29.setImageResource(R.drawable.fav2)

                // Trigger the delete callback
                onFavClicked(subCategory)

//            root.setOnClickListener {
//                onItemClick(subCategory)
//            }
        }}}}
//    }
//}
