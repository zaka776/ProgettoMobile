package com.progetto_zak.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class User(
    val firstname: String = "",
    val lastname: String = "",
    val email: String = ""
)

class UserViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val firstname = document.getString("firstname") ?: ""
                    val lastname = document.getString("lastname") ?: ""
                    val email = document.getString("email") ?: ""
                    _user.value = User(firstname,lastname, email)
                }
            }
            .addOnFailureListener {
                _user.value = User(firstname = "Errore", lastname = "Errore", email = "Errore")
            }
    }
}
