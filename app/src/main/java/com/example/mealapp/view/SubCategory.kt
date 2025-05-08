package com.example.mealapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentSubCategoryBinding
import com.example.mealapp.model.dataBase.DataBaseClient
import com.example.mealapp.model.dataBase.MealLocalClass
import com.example.mealapp.model.pojo.MealRepoImp
import com.example.mealapp.model.pojo.SubCategoryMeal
import com.example.mealapp.model.network.MealRemoteImp
import com.example.mealapp.model.network.ResponseState
import com.example.mealapp.model.network.RetrofitHelper
import com.example.mealapp.model.pojo.MealDataFav
import com.example.mealapp.viewModel.HomeViewFactory
import com.example.mealapp.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SubCategory : Fragment() {
    private var _binding: FragmentSubCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var subCategoryAdapter: SubCategoryAdapter
    private var subCategoryMeal: List<SubCategoryMeal> = emptyList()
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
    ): View? {
        _binding = FragmentSubCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = arguments?.getString("category") ?: ""

        subCategoryAdapter=SubCategoryAdapter(
            onItemClick = { categoryMeal ->
                val action = HomeFragmentDirections.actionHomeFragmentToSubCategory(categoryMeal.idMeal)
                findNavController().navigate(action)
            },
            onFavClicked = { categoryMeal ->
                val updatedCategory = categoryMeal.copy(isFavorite = !categoryMeal.isFavorite)

                // استبدال العنصر في القائمة الأصلية
                subCategoryMeal = subCategoryMeal.map {
                    if (it.idMeal == updatedCategory.idMeal) updatedCategory else it
                }

                // تحديث القائمة في الأدابتر
                subCategoryAdapter.submitList(subCategoryMeal.take(8))
                // Save to favorites
                val mealToFav = MealDataFav(
                    id = categoryMeal.idMeal.toLong(),
                    title = categoryMeal.strMeal,
                    img = categoryMeal.strMealThumb

                )
                if (updatedCategory.isFavorite) {

                    viewModel.insertMealToFav(mealToFav)
                    Log.d("fav", "Added to favorites: ${mealToFav.title}")
                } else {

                    viewModel.deleteMealFromDB(mealToFav)
                    Log.d("fav", "Removed from favorites: ${mealToFav.title}")
                }
            })
        binding.recyclersubCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = subCategoryAdapter
        }
            binding.imageView31.setOnClickListener{
                findNavController().navigate(R.id.action_subCategory_to_homeFragment)
            }
        //search
        binding.searchEditText1.addTextChangedListener {
            val newText = it.toString()
            val filteredList = subCategoryMeal.filter { category ->
                category.strMeal.startsWith(newText, ignoreCase = true)
            }
            subCategoryAdapter.submitList(filteredList.take(8))
        }

    viewModel.getSubCategory(category)

        lifecycleScope.launch(Dispatchers.Main) {

            viewModel.subCategory.collectLatest { viewStateResult ->
                when (viewStateResult) {
                    is ResponseState.Success -> {
                        subCategoryMeal = viewStateResult.data ?: emptyList()
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
