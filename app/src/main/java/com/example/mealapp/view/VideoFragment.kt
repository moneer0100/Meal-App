package com.example.mealapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentRecipeBinding
import com.example.mealapp.databinding.FragmentVideoBinding
import com.example.mealapp.model.MealRepoImp
import com.example.mealapp.model.Meals
import com.example.mealapp.model.toIngredientList
import com.example.mealapp.network.MealRemoteImp
import com.example.mealapp.network.ResponseState
import com.example.mealapp.network.RetrofitHelper
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
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView36.setOnClickListener{
            findNavController().navigate(R.id.action_videoFragment_to_recipe)
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
                            val meal = viewStateResult.data?.firstOrNull()
                            if (meal != null ) {
                            meal?.let {
                                binding.textView29.text = it.strMeal
                                binding.textViewDescription.text = it.strInstructions
                                val videoId = it.strYoutube?.substringAfter("v=")?.take(11)
                                videoId?.let { id ->
                                    youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                        override fun onReady(youTubePlayer: YouTubePlayer) {
                                            youTubePlayer.loadVideo(id, 0f)}})}}

                            } else {
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


