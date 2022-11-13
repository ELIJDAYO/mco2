package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityMainBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments.HomeFragment
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments.NotificationFragment
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments.SearchFragment
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments.UserFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    internal var selectedFragment: Fragment?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO how to use viewbinding in fragment
//        binding = ActivityMainBinding.bind(layoutInflater)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.home_toolbar))
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(HomeFragment())
    }
    private fun moveToFragment(fragment: Fragment)
    {
        val fragmentTrans=supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container,fragment)
        fragmentTrans.commit()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.nav_home -> {
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                moveToFragment(UserFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
                moveToFragment(NotificationFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_search -> {
                moveToFragment(SearchFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }
}


