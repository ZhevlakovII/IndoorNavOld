package com.example.indoornavigationar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.indoornavigationar.databinding.MainActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unity3d.player.UnityPlayerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: MainActivityBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_search ||
                destination.id == R.id.navigation_points_enter) {
                binding.navView.visibility = View.GONE
            } else {
                binding.navView.visibility = View.VISIBLE
            }
        }
        navView.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    fun hideActivity(cardActive: Boolean) {
        if (cardActive)
            binding.navView.visibility = View.GONE
        else
            binding.navView.visibility = View.VISIBLE
    }
}