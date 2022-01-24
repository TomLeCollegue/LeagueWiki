package com.androidcourse.leaguewiki

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidcourse.leaguewiki.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {

    lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.button.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToChampDetailFragment())
        }
    }
}
