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


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    internal var selectedFragment: Fragment?=null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
//            R.id.nav_search -> {
//                moveToFragment(SearchFragment())
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.nav_notifications -> {
//                moveToFragment(NotificationFragment())
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.nav_profile -> {
//                moveToFragment(ProfileFragment())
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }
    private fun moveToFragment(fragment:Fragment)
    {
        val fragmentTrans=supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container,fragment)
        fragmentTrans.commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO how to use viewbinding in fragment
//        binding = ActivityMainBinding.bind(layoutInflater)
        setContentView(R.layout.activity_main)

        //TODO apply this to login and register
        setSupportActionBar(findViewById(R.id.home_toolbar))

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
//TODO("check this later")
        val publisher = intent.getStringExtra("PUBLISHER_ID")
//        if(publisher!=null) {
//            val prefs: SharedPreferences.Editor? =
//                getSharedPreferences("PREFS", Context.MODE_PRIVATE)
//                    .edit().apply { putString("profileId", publisher); apply() }
//
//            moveToFragment(ProfileFragment())
//        }
//        else
//        //to call fragments
//            moveToFragment(HomeFragment())
    }


}


