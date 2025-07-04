package com.progetto_zak.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.progetto_zak.R
import com.progetto_zak.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Registrazione utente
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Le password non coincidono", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val firstname = binding.nameEditText.text.toString().trim()
            val lastname = binding.surnameEditText.text.toString().trim()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val db = FirebaseFirestore.getInstance()

                        val user = hashMapOf(
                            "firstname" to firstname,
                            "lastname" to lastname,
                            "email" to email
                        )

                        userId?.let {
                            db.collection("users").document(it).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Registrazione completata", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(requireContext(), "Errore nel salvataggio: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    }
                    else {
                        Toast.makeText(requireContext(), "Errore: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Link per tornare al Login
        binding.loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
