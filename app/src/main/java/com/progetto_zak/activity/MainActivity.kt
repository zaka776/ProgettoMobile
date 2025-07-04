package com.progetto_zak.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.progetto_zak.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // carica il layout appena modificato
    }
}
