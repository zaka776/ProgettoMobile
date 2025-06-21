package com.progetto_zak.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.progetto_zak.R
import com.progetto_zak.databinding.FragmentAuthChoiceBinding


class AuthChoiceFragment : Fragment() {
    private var _binding: FragmentAuthChoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_authChoiceFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_authChoiceFragment_to_registrationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
