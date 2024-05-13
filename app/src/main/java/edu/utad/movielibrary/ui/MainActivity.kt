package edu.utad.movielibrary.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import edu.utad.movielibrary.R
import edu.utad.movielibrary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.LoginFragment, R.id.signUpFragment -> toolBarHide()
                else -> toolBarShow()
            }
        }
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    private fun toolBarHide() {
        Log.d("debug", "Bar hided")
        binding.toolbar.visibility = View.GONE
    }

    private fun toolBarShow() {
        Log.d("debug", "Bar Visible")
        binding.toolbar.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_top_rated_movies -> {
                navController.navigate(R.id.topRatedMoviesFragment)
                true
            }

            R.id.action_popular_series -> {
                navController.navigate(R.id.popularSeriesFragment)
                true
            }

            R.id.action_favorites -> {
                navController.navigate(R.id.favoriteMoviesFragment)
                true
            }

            R.id.action_log_out -> {
                uid = ""
                navController.navigate(R.id.LoginFragment)
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

    fun setUserID(uid: String) {
        Log.d("debug", "set $uid")
        this.uid = uid
    }

    fun getUserID(): String {
        return this.uid
    }

}