package com.example.mygallerypractice

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.mygallerypractice.databinding.ActivityFrameBinding
import com.google.android.material.tabs.TabLayoutMediator

class FrameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFrameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val images = (intent.getStringArrayExtra("images") ?: emptyArray())
            .map{uriString -> FrameItem(Uri.parse(uriString))}
        val frameAdapter = FrameAdapter(images)

        binding.viewPager.adapter = frameAdapter

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
        ){
            tab,position ->
            binding.viewPager.currentItem = tab.position
        }.attach()

    }
}