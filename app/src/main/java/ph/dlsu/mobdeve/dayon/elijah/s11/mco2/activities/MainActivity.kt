package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityMainBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    internal var selectedFragment: Fragment?=null
    private lateinit var uid: String
    private lateinit var publisher:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.home_toolbar))
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        this.uid = FirebaseAuth.getInstance().currentUser!!.uid



        publisher = intent.getStringExtra("publisher_id").toString()
        val lastFragment = intent.getStringExtra("last_fragment")
        if(publisher != "null") {
            val prefs =
                getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    .edit().apply {
                        putString("profileId", publisher)
                        apply()
                    }
            moveToFragment(UserFragment())
        }

        else if (lastFragment == "userprofile"){
            val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .edit().apply {
                    putString("profileId", publisher)
                    apply()
                }
            moveToFragment(UserFragment())
        }
        else if (lastFragment == "home"){
            moveToFragment(HomeFragment())
        } else
            moveToFragment(HomeFragment())
    }
    private fun moveToFragment(fragment: Fragment)
    {
        //put bundle
        val bundle = Bundle()
        bundle.putString("publisher_id",publisher)
        fragment.arguments = bundle
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
                moveToFragment(SearchResultFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }
}


