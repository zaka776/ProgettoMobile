package com.progetto_zak.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {

    // Testo dinamico da mostrare nella schermata
    private val _welcomeText = MutableLiveData("Il tuo stile inizia qui!")
    val welcomeText: LiveData<String> get() = _welcomeText

    // Funzione chiamata quando premi "Continua"
    fun onContinueClicked() {
        _welcomeText.value = "Stai per iniziare..."
    }
}
