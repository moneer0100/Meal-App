package com.example.mealapp.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentHomeBinding
import com.example.mealapp.model.dataBase.DataBaseClient
import com.example.mealapp.model.dataBase.MealLocalClass
import com.example.mealapp.model.pojo.CategoryMeal
import com.example.mealapp.model.pojo.MealRepoImp
import com.example.mealapp.model.network.MealRemoteImp
import com.example.mealapp.model.network.ResponseState
import com.example.mealapp.model.network.RetrofitHelper
import com.example.mealapp.model.pojo.MealDataFav
import com.example.mealapp.viewModel.HomeViewFactory
import com.example.mealapp.viewModel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var isRandomMealFavorite = false
    private lateinit var homeAdapter: HomeAdapter
    private var originalCategories: List<CategoryMeal> = emptyList()
    private val viewModel: HomeViewModel by viewModels {
        HomeViewFactory(
            MealRepoImp.getInstance(
                MealRemoteImp.getInstance(RetrofitHelper.service),   MealLocalClass.getInstance(
                    DataBaseClient.getInstance(requireContext()).mealApp())
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeAdapter = HomeAdapter(
            onItemClick = { categoryMeal ->
                val action = HomeFragmentDirections.actionHomeFragmentToSubCategory(categoryMeal.strCategory)
                findNavController().navigate(action)
            },
            onFavClicked = { categoryMeal ->
                val updatedCategory = categoryMeal.copy(isFavorite = !categoryMeal.isFavorite)

                // استبدال العنصر في القائمة الأصلية
                originalCategories = originalCategories.map {
                    if (it.idCategory == updatedCategory.idCategory) updatedCategory else it
                }

                // تحديث القائمة في الأدابتر
                homeAdapter.submitList(originalCategories.take(8))
                // Save to favorites
                val mealToFav = MealDataFav(
                    id = categoryMeal.idCategory.toLong(),
                    title = categoryMeal.strCategory,
                    img = categoryMeal.strCategoryThumb

                )
                if (updatedCategory.isFavorite) {

                    viewModel.insertMealToFav(mealToFav)
                    Log.d("fav", "Added to favorites: ${mealToFav.title}")
                } else {

                    viewModel.deleteMealFromDB(mealToFav)
                    Log.d("fav", "Removed from favorites: ${mealToFav.title}")
                }
            })

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userName = user.displayName ?: user.email ?: "Unknown User"
            val firstName = userName.split(" ").firstOrNull()?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Unknown"
            binding.textView10.text = firstName
        } else {
            binding.textView10.text = "Guest"
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = homeAdapter
        }
        //search
        binding.searchEditText.addTextChangedListener {
            val newText = it.toString()
            val filteredList = originalCategories.filter { category ->
                category.strCategory.startsWith(newText, ignoreCase = true)
            }
            homeAdapter.submitList(filteredList.take(8))
        }


        viewModel.getRandomMeal()
        viewModel.getCategoryMeal()
        binding.textView17.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_seeAllCategory)

        }
        binding.imageView15.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_sideMenu)
        }

                lifecycleScope.launch(Dispatchers.Main) {
                    launch {
                        viewModel.randomMeal.collectLatest { viewStateResult ->
                            when (viewStateResult) {
                                is ResponseState.Success -> {
                                    val random = viewStateResult.data
                                    if (!random.isNullOrEmpty()) {
                                        val meal = random[0]

                                        // Set UI
                                        binding.textView13.text = meal.strCategory
                                        binding.textView14.text = meal.strArea
                                        binding.textView15.text = meal.strMeal
                                        Glide.with(requireContext())
                                            .load(meal.strMealThumb)
                                            .into(binding.imageView17)
                                        isRandomMealFavorite = false
                                        binding.imageView11.setImageResource(R.drawable.fav2)
                                        // ✅ Set click listener here
                                        binding.cardView.setOnClickListener {
                                            val action = HomeFragmentDirections.actionHomeFragmentToRecipe(
                                                id = meal.idMeal,
                                                source = "home"
                                            )
                                            findNavController().navigate(action)
                                        }
                                        binding.imageView11.setOnClickListener {
                                            isRandomMealFavorite = !isRandomMealFavorite
                                            if (isRandomMealFavorite) {
                                                // Insert into favorites
                                                val favMeal = MealDataFav(
                                                    id = meal.idMeal.toLong(),
                                                    title = meal.strMeal,
                                                    img = meal.strMealThumb
                                                )
                                                viewModel.insertMealToFav(favMeal)
                                                binding.imageView11.setImageResource(R.drawable.fav)
                                            } else {
                                                // Delete from favorites
                                                val favMeal = MealDataFav(
                                                    id = meal.idMeal.toLong(),
                                                    title = meal.strMeal,
                                                    img = meal.strMealThumb
                                                )
                                                viewModel.deleteMealFromDB(favMeal)
                                                binding.imageView11.setImageResource(R.drawable.fav2)
                                            }
                                        }
                                    }
                                }

                                is ResponseState.Error -> {
                                    binding.textView13.text = "Error loading meal"
                                }

                                is ResponseState.Loading -> {
                                    binding.textView13.text = "Loading..."
                                }
                            }
                        }
                    }
            launch {
                viewModel.categoryMeal.collectLatest { viewStateResult ->
                    when (viewStateResult) {
                        is ResponseState.Success -> {
                            originalCategories = viewStateResult.data ?: emptyList()
                            val categories = viewStateResult.data
                            if (categories != null) {
                                homeAdapter.submitList(categories)

                            }
                        }
                        is ResponseState.Error -> {

                        }
                        is ResponseState.Loading -> {

                        }
                    }
                }
            }
        }
    }
}
