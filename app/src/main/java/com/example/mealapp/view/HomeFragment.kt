package com.example.mealapp.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentHomeBinding
import com.example.mealapp.model.CategoryMeal
import com.example.mealapp.model.MealRepoImp
import com.example.mealapp.network.MealRemoteImp
import com.example.mealapp.network.ResponseState
import com.example.mealapp.network.RetrofitHelper
import com.example.mealapp.viewModel.HomeViewFactory
import com.example.mealapp.viewModel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeAdapter: HomeAdapter
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

        homeAdapter = HomeAdapter { categoryMeal ->
            // TODO: handle item click if needed
        }

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

        viewModel.getRandomMeal()
        viewModel.getCategoryMeal()
        binding.textView17.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_seeAllCategory)

        }
        lifecycleScope.launch(Dispatchers.Main) {
            launch {
                viewModel.randomMeal.collectLatest { viewStateResult ->
                    when (viewStateResult) {
                        is ResponseState.Success -> {
                            val random = viewStateResult.data
                            if (random != null && random.isNotEmpty()) {
                                binding.textView13.text = random[0].strCategory
                                binding.textView14.text = random[0].strArea
                                binding.textView15.text = random[0].strMeal
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

            launch {
                viewModel.categoryMeal.collectLatest { viewStateResult ->
                    when (viewStateResult) {
                        is ResponseState.Success -> {
                            val categories = viewStateResult.data?.take(8) // تحديد أول 8 عناصر فقط
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
