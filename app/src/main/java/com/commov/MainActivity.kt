package com.commov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.commov.network.UserAuth
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerView = findViewById<DrawerLayout>(R.id.drawer_layout)
        val menu = findViewById<NavigationView>(R.id.lateralMenu)
        val navController = findNavController(R.id.main_content_fragment)

        if(UserAuth.isLoggedIn){
            menu.menu.clear()
            menu.inflateMenu(R.menu.drawer_menu_user)
            menu.inflateMenu(R.menu.drawer_menu_visit)
        }

        menu.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId){
                R.id.notesList -> {
                    navController.navigate(R.id.notesList)
                }
                R.id.map -> {
                    navController.navigate(R.id.mapFragment)
                }
                R.id.myPoints -> {
                    // TODO
                }
            }
            drawerView.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
    }

}
