package com.progetto_zak.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.progetto_zak.R
import androidx.appcompat.app.AlertDialog



class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_home) // usa nav_graph_logged

        // Collega BottomNavigationView con NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_logged) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val appBarConfig = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfig)
    }

    /*override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Conferma uscita")
            .setMessage("Sei sicuro di voler uscire dall'app?")
            .setPositiveButton("Esci") { _, _ ->
                finishAffinity() // Chiude l'intera app
            }
            .setNegativeButton("Annulla", null)
            .show()
    }*/


}
