package com.example.maomakis.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.maomakis.R
import com.example.maomakis.databinding.ActivityMainBinding
import com.example.maomakis.databinding.NavHeaderMainBinding
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.CarritoViewModel
import com.example.maomakis.ui.viewmodel.ProductViewModel
import com.example.maomakis.ui.viewmodel.UserViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory(application, this)
    }
    private val carritoViewModel: CarritoViewModel by viewModels {
        ViewModelFactory(application, this)
    }
    private val productViewModel: ProductViewModel by viewModels {
        ViewModelFactory(application, this)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingNavHeader: NavHeaderMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val headerView = binding.navView.getHeaderView(0)
        bindingNavHeader = NavHeaderMainBinding.bind(headerView)

        setSupportActionBar(binding.appBarMain.toolbar)

        lifecycleScope.launch {
            userViewModel.loggedInUser.collect { user ->
                if (user != null) {
                    bindingNavHeader.txtViewUserName.text = user.displayName
                    bindingNavHeader.txtViewUserEmail.text = user.email
                }
            }
        }
        binding.buttonLogout.setOnClickListener{
            userViewModel.logout()
            goToBienvenidaActivity()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_comida_diaria, R.id.nav_favorito, R.id.nav_mi_carrito
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun goToBienvenidaActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Cerramos esta actividad para que no se pueda volver a ella
    }
}