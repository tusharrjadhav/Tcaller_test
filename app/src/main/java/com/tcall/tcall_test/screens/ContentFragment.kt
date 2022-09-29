package com.tcall.tcall_test.screens

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tcall.tcall_test.R
import com.tcall.tcall_test.databinding.FragmentContentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentFragment : Fragment() {

    private lateinit var binding: FragmentContentBinding
    private val viewModel: MainScreenViewModel by viewModels()

    companion object {
        fun newInstance() = ContentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = viewModel.dataModel
        binding.fragment = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::binding.isInitialized) {
            binding.unbind()
        }
    }

    fun fetchContent() {
        viewModel.fetchContent()
    }

}