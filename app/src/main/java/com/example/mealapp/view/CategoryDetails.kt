package com.example.mealapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentHomeBinding
import com.example.mealapp.databinding.FragmentRecipeBinding
import com.example.mealapp.databinding.FragmentSubCategoryBinding
import com.example.mealapp.model.MealRepoImp
import com.example.mealapp.model.toIngredientList
import com.example.mealapp.network.MealRemoteImp
import com.example.mealapp.network.ResponseState
import com.example.mealapp.network.RetrofitHelper
import com.example.mealapp.viewModel.HomeViewFactory
import com.example.mealapp.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryDetails : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    lateinit var categoryDetailsAdapter: CategoryDetailsAdapter
    private val viewModel: HomeViewModel by viewModels {
        HomeViewFactory(
            MealRepoImp.getInstance(
                MealRemoteImp.getInstance(RetrofitHelper.service)
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
        categoryDetailsAdapter=CategoryDetailsAdapter{}
        binding.recyclerView233.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = categoryDetailsAdapter
        }



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


