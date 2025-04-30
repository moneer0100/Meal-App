package com.example.mealapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mealapp.Login
import com.example.mealapp.R
import com.example.mealapp.databinding.FragmentHomeBinding
import com.example.mealapp.databinding.FragmentSideMenuBinding
import com.google.firebase.auth.FirebaseAuth


class SideMenu : Fragment() {
    private var _binding: FragmentSideMenuBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSideMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userName = user.displayName ?: user.email ?: "Unknown User"
            val firstName = userName.split(" ").firstOrNull()?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Unknown"
            binding.textView21.text = firstName

            val photoUrl = user.photoUrl
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.name) // صورة افتراضية
                    .into(binding.imageView23)
        } else {
            binding.textView21.text = "Guest"
        }
    }
        binding.imageView24.setOnClickListener{
            findNavController().navigate(R.id.action_sideMenu_to_homeFragment)
        }
        binding.imageView22.setOnClickListener{
            findNavController().navigate(R.id.action_sideMenu_to_homeFragment)
        }
        binding.imageView25.setOnClickListener{
            findNavController().navigate(R.id.action_sideMenu_to_favouriteScreen)
        }
        binding.imageView26.setOnClickListener {
            findNavController().navigate(R.id.action_sideMenu_to_historyScreen)
        }
        binding.imageView27.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
}}