package com.commov

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
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
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID: String? = "420"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerView: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        createNotificationChannel()
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().subscribeToTopic("issue")


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.drawerView = findViewById<DrawerLayout>(R.id.drawer_layout)
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
                    navController.navigate(R.id.myPointsFragment)
                }
            }
            this.drawerView.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
    }

    public fun openDrawer() {
        this.drawerView.openDrawer(Gravity.START)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
