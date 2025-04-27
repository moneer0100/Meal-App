package com.example.mealapp.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentHomeBinding
import com.example.mealapp.model.MealRepoImp
import com.example.mealapp.model.RandomMeal
import com.example.mealapp.network.MealRemoteImp
import com.example.mealapp.network.ResponseState
import com.example.mealapp.network.RetrofitHelper
import com.example.mealapp.viewModel.HomeViewFactory
import com.example.mealapp.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Call API
        viewModel.getRandomMeal()

        // Collect Flow
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.randomMeal.collectLatest { viewStateResult ->
                when (viewStateResult) {
                    is ResponseState.Success -> {
                        val random = viewStateResult.data
                        if (random != null && random.isNotEmpty()) {
                            binding.textView13.text = random[0].strCategory
                            binding.textView14.text=random[0].strArea
                            binding.textView15.text=random[0].strMeal
                            Glide.with(requireContext())
                                .load(random[0].strMealThumb)
                                .into(binding.imageView17)
                        } else {
                            binding.textView13.text = "No meal found"
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
    }
}
