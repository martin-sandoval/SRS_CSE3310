package com.example.sra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.ui.setupWithNavController
import com.example.sra.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),Login.OnBottomNavVisibilityListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView//



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = findViewById(R.id.bottom_navigation)//

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        //appBarConfiguration = AppBarConfiguration(setOf(R.id.Home)) current
        appBarConfiguration = AppBarConfiguration(setOf(R.id.Home, R.id.search, R.id.eventNotification))
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.Login || destination.id == R.id.Home ) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }else {
                supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show the Up button
            }
        }


        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.searchButton -> {navController.navigate(R.id.search)
                    true
                }
                R.id.home -> {
                    navController.navigate(R.id.Home)
                    true
                }
                R.id.notification ->{navController.navigate(R.id.eventNotification)
                    true
                }
                else -> false
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    //
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {

            R.id.Logout -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.Login)
                bottomNavigationView.visibility = View.GONE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
    override fun showBottomNavigationView() {
        bottomNavigationView.visibility = View.VISIBLE
    }
}