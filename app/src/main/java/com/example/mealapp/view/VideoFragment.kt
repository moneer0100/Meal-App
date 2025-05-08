package com.example.mealapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.mealapp.databinding.FragmentVideoBinding
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private lateinit var meal: Meals
    lateinit var categoryDetailsAdapter: CategoryDetailsAdapter
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
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView36.setOnClickListener{
            if (::meal.isInitialized) {
                val action = VideoFragmentDirections.actionVideoFragmentToRecipe(meal.idMeal)
                findNavController().navigate(action)
            } else {
                Log.e("VideoFragment", "Meal not initialized")
            }
        }
        val youTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)
        categoryDetailsAdapter=CategoryDetailsAdapter{}
        binding.recyclerView2.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = categoryDetailsAdapter
        }

        val id = arguments?.getString("mealId") ?: ""

        viewModel.getCategoryDetails(id)
        lifecycleScope.launch(Dispatchers.Main) {
            launch {
                viewModel.categoryDetails.collectLatest { viewStateResult ->
                    when (viewStateResult) {
                        is ResponseState.Success -> {
                            val currentMeal = viewStateResult.data?.firstOrNull()
                            if (currentMeal != null) {
                                currentMeal.let {
                                    binding.textView29.text = it.strMeal
                                    binding.textViewDescription.text = it.strInstructions
                                    meal = it
                                    val videoId = it.strYoutube?.substringAfter("v=")?.take(11)
                                    videoId?.let { id ->
                                        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                                youTubePlayer.loadVideo(id, 0f)
                                            }
                                        })
                                    }
                                }
                            }
                            else {
                                binding.textView29.text = "No meal found"
                            }
                        }
                        is ResponseState.Error -> {
                            binding.textView29.text = "Error loading meal"
                        }
                        is ResponseState.Loading -> {
                            binding.textView29.text = "Loading..."
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
                                binding.imageView36
                            }
                        }
                        is ResponseState.Error -> {
                            binding.textView29.text = "No categories found"
                        }
                        is ResponseState.Loading -> {
                            binding.textView29.text = "Loading..."
                        }
                    }
                }
            }
        }
    }
    }


