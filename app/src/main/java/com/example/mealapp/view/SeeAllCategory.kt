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
import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentSeeAllCategoryBinding
import com.example.mealapp.model.dataBase.DataBaseClient
import com.example.mealapp.model.dataBase.MealLocalClass
import com.example.mealapp.model.pojo.MealRepoImp
import com.example.mealapp.model.network.MealRemoteImp
import com.example.mealapp.model.network.ResponseState
import com.example.mealapp.model.network.RetrofitHelper
import com.example.mealapp.model.pojo.MealDataFav
import com.example.mealapp.viewModel.HomeViewFactory
import com.example.mealapp.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SeeAllCategory : Fragment() {

    private lateinit var homeAdapter: HomeAdapter
    private var _binding: FragmentSeeAllCategoryBinding? = null
    private val binding get() = _binding!!
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
        // Inflate the layout for this fragment
        _binding = FragmentSeeAllCategoryBinding.inflate(inflater, container, false)
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
                // Save to favorites
                val mealToFav = MealDataFav(
                    id = categoryMeal.idCategory.toLong(),
                    title = categoryMeal.strCategory,
                    img = categoryMeal.strCategoryThumb

                )
                viewModel.insertMealToFav(mealToFav)
            })

        binding.recyclerViewCategory.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = homeAdapter
        }
        viewModel.getCategoryMeal()
        binding.imageView20.setOnClickListener {
            findNavController().navigate(R.id.action_seeAllCategory_to_homeFragment)
        }

        lifecycleScope.launch(Dispatchers.Main){
            viewModel.categoryMeal.collectLatest { viewStateResult ->
                when (viewStateResult) {
                    is ResponseState.Success -> {
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


    }}
