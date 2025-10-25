package com.example.maomakis.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.maomakis.R
import com.example.maomakis.databinding.ActivityAuthBinding
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.AuthViewModel

class AuthActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(application, this)
    }

    private lateinit var b: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authViewModel.isUserLoggedIn()) {
            goToMainActivity()
            return
        }
        b = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_auth)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}