package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
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
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.OptionActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.ViewPagerAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentHomeBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentUserBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.User


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
    private  var storageProfileRef:StorageReference?=null
    private  var imageUri: Uri?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val pref = context?.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        if (pref != null) {
            this.profileId = pref.getString("profileId","")!!
        }
        //for now use this
        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid
        init()

        if (profileId == firebaseUser.uid) {
            binding.ivFollow.background = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)
            binding.ivEdit.visibility = View.VISIBLE
            binding.ivSetting.visibility = View.VISIBLE
        } else if (profileId != firebaseUser.uid) {
            Toast.makeText(activity,"$profileId vs $firebaseUser.uid",Toast.LENGTH_SHORT).show()
            binding.ivFollow.background = ResourcesCompat.getDrawable(resources,R.drawable.heart_edge, null)
            binding.ivEdit.visibility = View.INVISIBLE
            binding.ivSetting.visibility = View.INVISIBLE
            checkFollowOrFollowingButtonStatus()
        }
        binding.ivFollow.setOnClickListener {
            if(binding.tvIsfollowed.text == "no"){
                binding.tvIsfollowed.text = "yes"
                binding.ivFollow.background = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)
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
                binding.tvIsfollowed.text = "no"
                binding.ivFollow.background = ResourcesCompat.getDrawable(resources,R.drawable.heart_edge, null)
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
        binding.cmvImg.setOnClickListener {
            uploadProfileImg()
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
        setNumFollowers()
        setUserInfo()


    }
    private fun uploadProfileImg(){

    }
    private fun setUserInfo(){
        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(profileId)
        userRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.superhero).into(binding.cmvImg)
                    binding.tvUsername.text = user.getUsername()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun setNumFollowers(){
        val followersRef = firebaseUser.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(profileId)
                .child("Followers")
        }
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
                        binding.ivFollow.background = drawable
//                        view?.edit_profile_Button?.text = "Following"
                    } else {
                        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.heart_edge, null)
                        binding.ivFollow.background = drawable                    }
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