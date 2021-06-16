package com.jugal.trainingsample.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.plusAssign
import com.jugal.trainingsample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findNavController(R.id.main_nav_host_fragment).navigatorProvider.apply {
            this += DialogFragmentNavigator(
                this@MainActivity,
                supportFragmentManager
            )
        }
    }

    override fun onNavigateUp(): Boolean {
        return findNavController(R.id.main_nav_host_fragment).navigateUp()
    }
}