package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.OptionActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.ViewPagerAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentHomeBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentUserBinding


class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val tabArray = arrayOf(
        "Recent Works",
        "Bookmark",
        "Followed"
    )
    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val follow = ResourcesCompat.getDrawable(resources,R.drawable.heart_edge, null)
        val followed = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)


        if (pref != null) {
            this.profileId = pref.getString("profileId", "none")!!
        }

        init()
        if (profileId == firebaseUser.uid) {
            binding.ibFollow.background = followed
            binding.ivEdit.visibility = View.VISIBLE
            binding.ivSetting.visibility = View.VISIBLE
        } else if (profileId != firebaseUser.uid) {
            binding.ibFollow.background = follow
            binding.ivEdit.visibility = View.INVISIBLE
            binding.ivSetting.visibility = View.INVISIBLE
            checkFollowOrFollowingButtonStatus()
        }
        binding.ibFollow.setOnClickListener {
            if(binding.ibFollow.drawable == follow){
                binding.ibFollow.background = followed
                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it1.toString())
                        .child("Following").child(profileId)
                        .setValue(true)

//                    pushNotification()
                }

                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(profileId)
                        .child("Followers").child(it1.toString())
                        .setValue(true)
                }
            }else{
                binding.ibFollow.background = follow
                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it1)
                        .child("Following").child(profileId)
                        .removeValue()
                }
                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(profileId)
                        .child("Followers").child(it1)
                        .removeValue()
                }
            }
        }

        binding.ivSetting.setOnClickListener{
            startActivity(Intent(context, OptionActivity::class.java))
        }
        binding.ivEdit.setOnClickListener{
            startActivity(Intent(context, EditNovelActivity::class.java))
        }
        return binding.root
    }
    private fun init(){
        val followersRef = firebaseUser.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it)
                .child("Following").child(profileId)
        }
        if(followersRef != null){
            followersRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.childrenCount
                    binding.tvFollowers.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
    private fun checkFollowOrFollowingButtonStatus() {

        val followingRef = firebaseUser.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1)
                .child("Following")
        }

        if (followingRef != null) {
            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.child(profileId).exists()) {
                        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)
                        binding.ibFollow.background = drawable
//                        view?.edit_profile_Button?.text = "Following"
                    } else {
                        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.heart_edge, null)
                        binding.ibFollow.background = drawable                    }
                }
            })
        }
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabs
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}