package com.firmfreez.android.androidemptytemplate.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.firmfreez.android.androidemptytemplate.view.viewModels.FragmentMainViewModel
import com.firmfreez.android.androidemptytemplate.R
import com.firmfreez.android.androidemptytemplate.databinding.FragmentMainBinding

class FragmentMain : BaseFragment() {
    private lateinit var binding: FragmentMainBinding

    companion object {
        fun newInstance() = FragmentMain()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        binding = FragmentMainBinding.bind(view)
        binding.viewModel = ViewModelProvider(this).get(FragmentMainViewModel::class.java)
        setToolbar(R.string.app_name, false, binding.root)

        return binding.root
    }
}