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
            // محاولة الحصول على الاسم
            val rawName = when {
                !user.displayName.isNullOrEmpty() -> {
                    user.displayName!!.split(" ").firstOrNull()
                }
                !user.email.isNullOrEmpty() -> {
                    user.email!!.substringBefore("@").substringBefore("+")
                }
                else -> null
            }

            // تنظيف الاسم: إزالة الأرقام وتنسيقه
            val cleanName = rawName
                ?.replace(Regex("\\d"), "") // إزالة الأرقام
                ?.replace(Regex("[^A-Za-z]"), "") // إزالة الرموز
                ?.lowercase()
                ?.replaceFirstChar { it.uppercase() } ?: "Unknown"

            binding.textView21.text = cleanName

            // تحميل الصورة
            val photoUrl = user.photoUrl
            Glide.with(this)
                .load(photoUrl ?: R.drawable.name) // استخدم صورة افتراضية إذا مفيش صورة
                .into(binding.imageView23)

        } else {
            // المستخدم مش مسجل دخول
            binding.textView21.text = "Guest"
            Glide.with(this)
                .load(R.drawable.name)
                .into(binding.imageView23)
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