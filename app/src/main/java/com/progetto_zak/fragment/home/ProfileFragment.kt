package com.progetto_zak.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.progetto_zak.R
import com.progetto_zak.databinding.FragmentProfileBinding
import com.progetto_zak.viewmodel.UserViewModel
import com.progetto_zak.activity.MainActivity
import androidx.appcompat.app.AlertDialog



class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Osserva i dati dell'utente dal ViewModel
        viewModel.user.observe(viewLifecycleOwner) { user ->
            val fullname = "${user.firstname} ${user.lastname}"
            binding.tvUserName.text = fullname
            binding.tvUserEmail.text = user.email
        }

        binding.menuLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Conferma logout")
                .setMessage("Vuoi uscire dal tuo account?")
                .setPositiveButton("Logout") { _, _ ->
                    logoutUser()
                }
                .setNegativeButton("Annulla", null)
                .show()
        }
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()

        GoogleSignIn.getClient(
            requireContext(),
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        ).signOut().addOnCompleteListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("fromLogout", true)  // passaggio extra!
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
