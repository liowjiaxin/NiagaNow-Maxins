package com.example.niaganow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class BottomNavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_nav_activity)

        val navView = findViewById<BottomNavigationView>(R.id.bottomNav)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // Handle bottom nav manually
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    if (navController.currentDestination?.id != R.id.dashboardFragment) {
                        navController.navigate(R.id.dashboardFragment)
                    }
                    true

                }
                R.id.nav_loans -> {
                    if (navController.currentDestination?.id != R.id.loansFragment) {
                        navController.navigate(R.id.loansFragment)
                    }
                    true
                }
                R.id.nav_qr -> {
                    if (navController.currentDestination?.id != R.id.QRFragment) {
                        navController.navigate(R.id.QRFragment)
                    }
                    true
                }
                R.id.nav_history -> {
                    if (navController.currentDestination?.id != R.id.TransactionHistory) {
                        navController.navigate(R.id.TransactionHistory)
                    }
                    true
                }

                R.id.nav_profile -> {
                    if (navController.currentDestination?.id != R.id.userProfileFragment) {
                        navController.navigate(R.id.userProfileFragment)
                    }
                    true
                }

                else -> false
            }
        }

        // Optional: Keep nav selected when navigating
        navController.addOnDestinationChangedListener { _, destination, _ ->
            navView.menu.findItem(destination.id)?.isChecked = true
        }
    }
}
