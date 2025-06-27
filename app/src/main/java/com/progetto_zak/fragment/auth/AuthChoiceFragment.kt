package com.progetto_zak.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.progetto_zak.R
import com.progetto_zak.databinding.FragmentAuthChoiceBinding
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.credentials.*
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.progetto_zak.activity.HomeActivity
import kotlinx.coroutines.launch



class AuthChoiceFragment : Fragment() {
    private var _binding: FragmentAuthChoiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var credentialManager: CredentialManager
    private lateinit var auth: FirebaseAuth

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

        credentialManager = CredentialManager.create(requireContext())
        auth = FirebaseAuth.getInstance()

        binding.btnGoogleSignIn.setOnClickListener {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id)) // <-- ID dal file JSON
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = requireContext()
                    )
                    handleGoogleCredential(result.credential)
                } catch (e: Exception) {
                    Log.e("GOOGLE_SIGNIN", "Errore: ${e.message}")
                    Toast.makeText(requireContext(), "Errore nel login: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleGoogleCredential(credential: Credential) {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        val userId = firebaseUser?.uid ?: return@addOnCompleteListener

                        val user = hashMapOf(
                            "firstname" to (firebaseUser.displayName?.split(" ")?.firstOrNull() ?: ""),
                            "lastname" to (firebaseUser.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: ""),
                            "email" to (firebaseUser.email ?: "")
                        )

                        FirebaseFirestore.getInstance().collection("users").document(userId).set(user)
                            .addOnSuccessListener {
                                val intent = Intent(requireContext(), HomeActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Errore nel salvataggio utente", Toast.LENGTH_SHORT).show()
                            }

                    } else {
                        Toast.makeText(requireContext(), "Autenticazione fallita", Toast.LENGTH_SHORT).show()
                    }
                }

        } else {
            Log.w("GOOGLE_SIGNIN", "Credenziale non valida")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
