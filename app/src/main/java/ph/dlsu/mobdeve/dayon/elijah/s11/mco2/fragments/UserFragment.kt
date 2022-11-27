package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.with
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.OptionActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.ViewPagerAdapter
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
    private var storageProfileRef: StorageReference?=null
    private  var myUrl=""
    private  var imageUri: Uri?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfileRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")

        //for now use this
        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid
        init()

        if (profileId == firebaseUser.uid) {
            binding.ivFollow.background = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)
            binding.ivEdit.visibility = View.VISIBLE
            binding.ivSetting.visibility = View.VISIBLE
        } else if (profileId != firebaseUser.uid) {
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
            deleteProfileImage()
            val galleryIntent = Intent(Intent.ACTION_PICK)
//             here item is type of image
            galleryIntent.type = "image/*"
//             ActivityResultLauncher callback
            imagePickerActivityResult.launch(galleryIntent)
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

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // getting URI of selected Image
                val imageUri: Uri? = result.data?.data

                // val fileName = imageUri?.pathSegments?.last()

                // extract the file name with extension
                val sd = activity?.let { getFileName(it.applicationContext, imageUri!!) }
                // Upload Task with upload to directory 'file'
                // and name of the file remains same
                val uploadTask = storageProfileRef?.child("$profileId/$sd")?.putFile(imageUri!!)
                // On success, download the file URL and display it
                uploadTask?.addOnSuccessListener {
                    val downloadUrl = storageProfileRef?.child("$profileId/$sd")?.downloadUrl
                    // using glide library to display the image
                    downloadUrl?.addOnSuccessListener {
                        Glide.with(this)
                            .load(it)
                            .into(binding.cmvImg)
                        val ref=FirebaseDatabase.getInstance().reference.child("Users")
                        val userMap = HashMap<String, Any>()
                        userMap["image"] = it.toString()
                        ref.child(firebaseUser.uid).updateChildren(userMap)

                        Log.e("Firebase", "download passed")
                    }?.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }?.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }
    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
    private fun deleteProfileImage(){
        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(profileId)
        userRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(User::class.java)
                    val uri = user!!.getImage()
                    val ref = FirebaseStorage.getInstance().getReferenceFromUrl(uri)
                    Toast.makeText(context,"$ref",Toast.LENGTH_SHORT).show()
                    ref.delete()

                    val userMap= java.util.HashMap<String, Any>()
                    userMap["image"]= ""
                    userRef.child(profileId).setValue(userMap)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun setUserInfo(){
        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(profileId)
        userRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(User::class.java)
                    val uri = user?.getImage()
                    if(!uri.isNullOrBlank()){ // Pass it to Picasso to download, show in ImageView and caching
                        Picasso.get().load(uri.toString()).placeholder(R.drawable.superhero).into(binding.cmvImg)
                    }
                    if (user != null) {
                        binding.tvUsername.text = user.getUsername()
                    }
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