package com.progetto_zak.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.progetto_zak.databinding.FragmentWelcomeBinding
import androidx.navigation.fragment.findNavController
import com.progetto_zak.R
import com.progetto_zak.viewmodel.WelcomeViewModel

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fromLogout = activity?.intent?.getBooleanExtra("fromLogout", false) ?: false

        if (fromLogout) {
            findNavController().navigate(R.id.action_welcomeFragment_to_authChoiceFragment)
            return
        }

        // Azione al click del bottone "Prosegui"
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_authChoiceFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
