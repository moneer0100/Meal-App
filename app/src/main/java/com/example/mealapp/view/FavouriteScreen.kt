package com.example.mealapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentFavouriteScreenBinding
import com.example.mealapp.databinding.FragmentHomeBinding
import com.example.mealapp.model.dataBase.DataBaseClient
import com.example.mealapp.model.dataBase.MealLocalClass
import com.example.mealapp.model.network.MealRemoteImp
import com.example.mealapp.model.network.ResponseState
import com.example.mealapp.model.network.RetrofitHelper
import com.example.mealapp.model.pojo.MealDataFav
import com.example.mealapp.model.pojo.MealRepoImp
import com.example.mealapp.model.pojo.SubCategoryMeal
import com.example.mealapp.viewModel.HomeViewFactory
import com.example.mealapp.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouriteScreen : Fragment() {
    private lateinit var favAdapter: FavAdapter
    private var mealFav: List<MealDataFav> = emptyList()
    private var _binding: FragmentFavouriteScreenBinding? = null
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

        _binding = FragmentFavouriteScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //search
        binding.searchEditText5.addTextChangedListener {
            val newText = it.toString()
            val filteredList = mealFav.filter { category ->
                category.title?.startsWith(newText, ignoreCase = true) ?:false
            }
            favAdapter.submitList(filteredList.take(8))
        }
        favAdapter=FavAdapter{
            meal->
            viewModel.deleteMealFromDB(meal)
            mealFav = mealFav.filterNot { it.id == meal.id }
            favAdapter.submitList(mealFav.toList()) // submit new list

        }
        binding.recyclerFav.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favAdapter
        }

        viewModel.getMealFav()
        lifecycleScope.launch(Dispatchers.Main){
            viewModel.fav.collectLatest { result->
                when(result){
                    is ResponseState.Success->{
                        mealFav = result.data ?: emptyList()
                        val categories = result.data
                        if (categories != null) {
                            favAdapter.submitList(categories)
                        }

                    }
                    is ResponseState.Loading->{

                    }
                    is ResponseState.Error->{}
                }
            }
        }

    }

}