package com.example.mealapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentHomeBinding
import com.example.mealapp.databinding.FragmentSubCategoryBinding
import com.example.mealapp.model.MealRepoImp
import com.example.mealapp.network.MealRemoteImp
import com.example.mealapp.network.ResponseState
import com.example.mealapp.network.RetrofitHelper
import com.example.mealapp.viewModel.HomeViewFactory
import com.example.mealapp.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SubCategory : Fragment() {
    private var _binding: FragmentSubCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var subCategoryAdapter: SubCategoryAdapter
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
        _binding = FragmentSubCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = arguments?.getString("category") ?: ""

        subCategoryAdapter=SubCategoryAdapter{subCategoryMeal ->  }
        binding.recyclersubCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = subCategoryAdapter
        }
            binding.imageView31.setOnClickListener{
                findNavController().navigate(R.id.action_subCategory_to_homeFragment)
            }

    viewModel.getSubCategory(category)

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.subCategory.collectLatest { viewStateResult ->
                when (viewStateResult) {
                    is ResponseState.Success -> {
                        val categories = viewStateResult.data
                        if (categories != null) {
                            subCategoryAdapter.submitList(categories)
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
