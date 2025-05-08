package com.example.mealapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentRecipeBinding
import com.example.mealapp.model.dataBase.Dao
import com.example.mealapp.model.dataBase.DataBaseClient
import com.example.mealapp.model.dataBase.MealLocalClass
import com.example.mealapp.model.pojo.MealRepoImp
import com.example.mealapp.model.pojo.Meals
import com.example.mealapp.model.pojo.toIngredientList
import com.example.mealapp.model.network.MealRemoteImp
import com.example.mealapp.model.network.ResponseState
import com.example.mealapp.model.network.RetrofitHelper
import com.example.mealapp.viewModel.HomeViewFactory
import com.example.mealapp.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryDetails : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var meal: Meals
    lateinit var categoryDetailsAdapter: CategoryDetailsAdapter
    private val viewModel: HomeViewModel by viewModels {
        HomeViewFactory(
            MealRepoImp.getInstance(
                MealRemoteImp.getInstance(RetrofitHelper.service),   MealLocalClass.getInstance(DataBaseClient.getInstance(requireContext()).mealApp())
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: CategoryDetailsArgs by navArgs()
        binding.imageView32.setOnClickListener {
            if (::meal.isInitialized) {
                when (args.source) {
                    "subcategory" -> findNavController().navigate(R.id.action_recipe_to_subCategory)
                    "home" -> findNavController().navigate(R.id.action_recipe_to_homeFragment)
                }
            }
        }
        categoryDetailsAdapter=CategoryDetailsAdapter{}
        binding.recyclerView233.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = categoryDetailsAdapter
        }

            binding.button3.setOnClickListener{
                if (::meal.isInitialized) {
                val action = CategoryDetailsDirections.actionRecipeToVideoFragment( meal.idMeal)
                findNavController().navigate(action)
            }}

        val id = arguments?.getString("id") ?: ""

            viewModel.getCategoryDetails(id)
        lifecycleScope.launch(Dispatchers.Main) {
            launch {
                viewModel.categoryDetails.collectLatest { viewStateResult ->
                    when (viewStateResult) {
                        is ResponseState.Success -> {
                            val random = viewStateResult.data
                            if (random != null && random.isNotEmpty()) {
                                binding.textView28.text = random[0].strMeal
                                meal = random[0]
                                Glide.with(requireContext())
                                    .load(random[0].strMealThumb)
                                    .into(binding.imageView35)
                            } else {
                                binding.textView28.text = "No meal found"
                            }
                        }
                        is ResponseState.Error -> {
                            binding.textView28.text = "Error loading meal"
                        }
                        is ResponseState.Loading -> {
                            binding.textView28.text = "Loading..."
                        }
                    }
                }
            }

            launch {
                viewModel.categoryDetails.collectLatest { viewStateResult ->
                    when (viewStateResult) {
                        is ResponseState.Success -> {
                            val categories = viewStateResult.data
                            if (categories != null && categories.isNotEmpty()) {
                                val ingredientList = categories[0].toIngredientList()
                                categoryDetailsAdapter.submitList(ingredientList)
                            }
                        }
                        is ResponseState.Error -> {
                            binding.textView28.text = "No categories found"
                        }
                        is ResponseState.Loading -> {
                            binding.textView28.text = "Loading..."
                        }
                    }
                }
            }
        }
    }
    }


