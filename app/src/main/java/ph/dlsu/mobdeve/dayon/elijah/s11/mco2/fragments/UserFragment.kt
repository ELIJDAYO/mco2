package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.OptionActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.UserAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentUserBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.User
import kotlin.system.measureTimeMillis


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
    private lateinit var sharedPreferences:SharedPreferences
    private var novelDateUpdatedList = arrayListOf<String>()
    private var recentNovels = arrayListOf<Novel>()
    private var bookmarkNovels = arrayListOf<Novel>()
    private var bookmarkNovelIds = arrayListOf<String>()

    private var followedUsersIds = arrayListOf<String>()
    private var followedUsers = arrayListOf<User>()

    lateinit var episodeRef: DatabaseReference
    lateinit var novelRef: DatabaseReference
    lateinit var userRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun init(){
        var publisher = arguments?.getString("publisher_id").toString()
        if(publisher != "null"){
            val prefs =
                this.activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    ?.edit().apply {
                        this!!.putString("profileId", publisher)
                        this.apply()
                    }
            this.profileId = publisher
//            Log.e(TAG,"A UserFragment: ${publisher} vs ${profileId}")
        }
        else{
            this.profileId = FirebaseAuth.getInstance().currentUser!!.uid
//            Log.e(TAG,"B UserFragment: ${publisher} vs ${profileId}")
        }
        checkFollowOrFollowingButtonStatus()
        if (profileId == firebaseUser.uid) {
            binding.ivEdit.visibility = View.VISIBLE
            binding.ivSetting.visibility = View.VISIBLE
        } else {
            binding.ivEdit.visibility = View.INVISIBLE
            binding.ivSetting.visibility = View.INVISIBLE
        }
        setNumFollowers()
        setUserInfo()
        CoroutineScope(Main).launch{
            readData1()
            val adapter = context?.let { NovelItemAdapter(it,recentNovels,novelDateUpdatedList ) }!!
            binding.rv.adapter = adapter

        }
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
                    if(uri == "R.drawable.superhero"){
                        return
                    }
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
                    Log.e(TAG,"This is the profileUri $uri")
                    if(uri!=""){ // Pass it to Picasso to download, show in ImageView and caching
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

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(profileId).exists()) {
                    val drawable = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)
                    binding.ivFollow.background = drawable
                    binding.tvIsfollowed.text = "yes"
//                        view?.edit_profile_Button?.text = "Following"
                } else {
                    val drawable = ResourcesCompat.getDrawable(resources, R.drawable.heart_edge, null)
                    binding.ivFollow.background = drawable
                    binding.tvIsfollowed.text = "no"
                }
            }
        })
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfileRef = FirebaseStorage.getInstance().reference.child("Pictures")

        init()
        //temporary
        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid
        binding.rv.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)

        binding.ivFollow.setOnClickListener {
            if(binding.tvIsfollowed.text == "no"){
                binding.tvIsfollowed.text = "yes"
                binding.ivFollow.background = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)
                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it1.toString())
                        .child("Following").child(profileId)
                        .setValue(profileId)

//                    pushNotification()
                }

                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(profileId)
                        .child("Followers").child(it1.toString())
                        .setValue(it1.toString())
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
        binding.btn1.setOnClickListener {
            val adapter = context?.let { NovelItemAdapter(it,recentNovels,novelDateUpdatedList ) }!!
            binding.rv.adapter = adapter
        }
        binding.btn2.setOnClickListener {
            var tmp = arrayListOf<String>()
            for(element in bookmarkNovels){
                tmp.add("")
            }
            val adapter = context?.let { NovelItemAdapter(it,bookmarkNovels,tmp) }!!
            binding.rv.adapter = adapter
        }
        binding.btn3.setOnClickListener {
            val adapter = context?.let { UserAdapter(it,followedUsers) }!!
            binding.rv.adapter = adapter
        }
    }
    private suspend fun readData1(){
        withContext(Main){
            val executionTime = measureTimeMillis {
                async {
                    println("debug: launching 1st job: ${Thread.currentThread().name}")
                    fetchRecentNovels()
                }.await()
                async {
                    println("debug: launching 2nd job: ${Thread.currentThread().name}")
                    fetchUpdateDateNovel()
                }.await()
                async {
                    println("debug: launching 3rd job: ${Thread.currentThread().name}")
                    fetchBookmarksIds()
                }.await()
                async {
                    println("debug: launching 4th job: ${Thread.currentThread().name}")
                    fetchBookmarks()
                }.await()
                async {
                    println("debug: launching 5th job: ${Thread.currentThread().name}")
                    fetchFollowingIds()
                }.await()
                async {
                    println("debug: launching 6th job: ${Thread.currentThread().name}")
                    fetchFollowing()
                }.await()
            }
            Log.e(ContentValues.TAG,"debug: job1 and job2 are complete. It took ${executionTime} ms")

        }
    }
    private suspend fun fetchRecentNovels(){
        withContext(Main){
            novelRef = FirebaseDatabase.getInstance().getReference("Novels")
            var query = novelRef.orderByChild("novelId")
            query.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        recentNovels.clear()
                        for (element in snapshot.children){
                            val novel = element.getValue(Novel::class.java)
//                            Log.e(TAG,"userfragment: what in here: ${novel!!.getTitle()} has ${novel.getNumEpisodes()} episodes")

                            if(novel!!.getNumEpisodes().toInt() > 0){
                                recentNovels.add(novel!!)
                            }
                        }
                    }
//                    Log.e(TAG,"userfragment: 0 what in here: $recentNovels")

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            delay (700)
//            Log.e(TAG,"userfragment: what in here: $recentNovels")
        }
    }
    private suspend fun fetchUpdateDateNovel(){
        withContext(Main) {
            var tmp = arrayListOf<String>()
            for(element in recentNovels){
                tmp.add(element.getNovelId())
            }
            Log.e(TAG,"Crushing point $recentNovels")
            episodeRef = FirebaseDatabase.getInstance().getReference("Episodes")
            var query = episodeRef.orderByChild("releaseDateTime").limitToLast(100)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        novelDateUpdatedList.clear()
                        for (element in snapshot.children) {
                            val episode = element.getValue(Episode::class.java)
                            if(episode!!.getReleaseDateTime().isBlank()){
                                Log.e(TAG,"This is blank")
                            }
                            else if(episode.getNovelId() in tmp){
                                novelDateUpdatedList.add(episode.getReleaseDateTime())
                                tmp.remove(episode.getNovelId())
                            }
//                            Log.e(TAG,"userfragment: whats in episode ${episode.getReleaseDateTime()}")
//                            Log.e(TAG,"userfragment: whats in tmp ${tmp}")
//                            Log.e(TAG,"userfragment: whats in tmp size ${tmp.size} novelDateUpdatelis size ${novelDateUpdatedList.size}")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "$error")
                }
            })
            delay(700)
            Log.e(TAG,"Crushing point $novelDateUpdatedList")
        }
    }
    private suspend fun fetchBookmarksIds(){
        withContext(Main){
            novelRef = FirebaseDatabase.getInstance().reference
                .child("Bookmark_Follow")
                .child(profileId)
                .child("Bookmark_Followers")
            novelRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        bookmarkNovelIds.clear()
                        for (element in snapshot.children){
                            val novelId = element.getValue(String::class.java)
                            Log.e(TAG,"number of bookmarkIds $novelId")

                            bookmarkNovelIds.add(novelId!!)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            delay (500)
        }
    }
    private suspend fun fetchBookmarks(){
        withContext(Main){
            novelRef = FirebaseDatabase.getInstance().getReference("Novels")
            novelRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        bookmarkNovels.clear()
                        for (element in snapshot.children){
                            val novel = element.getValue(Novel::class.java)
                            if(novel!!.getNovelId() in bookmarkNovelIds){
                                bookmarkNovels.add(novel)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            delay (300)
            Log.e(TAG,"number of bookmark novels $bookmarkNovels")
        }
    }
    private suspend fun fetchFollowingIds(){
        withContext(Main){
            userRef = FirebaseDatabase.getInstance().getReference("Follow/$profileId/Following/")
            userRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        followedUsersIds.clear()
                        for (element in snapshot.children){
                            val userId = element.getValue(String::class.java)
                            followedUsersIds.add(userId!!)
                            Log.e(TAG,"Display UserId ${element.key}")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            delay (300)
        }
    }
    private suspend fun fetchFollowing(){
        withContext(Main){
            userRef = FirebaseDatabase.getInstance().getReference("Users")
            userRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        followedUsers.clear()
                        for (element in snapshot.children){
                            val user = element.getValue(User::class.java)
                            if(user!!.getUid() in followedUsersIds){
                                followedUsers.add(user)

                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            delay (300)

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}