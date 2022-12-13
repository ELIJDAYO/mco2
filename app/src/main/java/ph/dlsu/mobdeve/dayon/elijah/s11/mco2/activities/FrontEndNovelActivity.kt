package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelEpisodeAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.TagAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityFrontEndNovelBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Tag
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.User
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.measureTimeMillis


class FrontEndNovelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFrontEndNovelBinding
    private lateinit var episodeAdapter: NovelEpisodeAdapter
    private lateinit var tagAdapter: TagAdapter

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var profileId: String
    private lateinit var novelId:String
    lateinit var novelRef: DatabaseReference
    lateinit var episodeRef: DatabaseReference
    lateinit var tagRef: DatabaseReference

    private  var episodeList= arrayListOf<Episode>()
    private  var tagList= arrayListOf<Tag>()


    private  var uid = ""
    private lateinit var author:String
    lateinit var userRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrontEndNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvEpisodes.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvTags.layoutManager = LinearLayoutManager(applicationContext)

        setupNovelDetail()
        setupEpisodeList()
        fetchAuthor()

        episodeAdapter = NovelEpisodeAdapter(this, episodeList,"view")
        binding.rvEpisodes.adapter = episodeAdapter
        CoroutineScope(IO).launch{
            readData1()
        }


        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        this.profileId = firebaseUser.uid


        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.ivNovel.visibility = View.INVISIBLE
        binding.ivEps.visibility = View.INVISIBLE
        binding.ivClose.setOnClickListener {
            val intent= Intent(this@FrontEndNovelActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("last_fragment","home")
            startActivity(intent)
            finish()
        }
        binding.ibBookmark.setOnClickListener {
            //make fun if its bookmarked 12-14-2022
            if(binding.tvIsfollowed.text == "no"){
                binding.tvIsfollowed.text = "yes"
                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Bookmark_Follow").child(it1.toString())
                        .child("Bookmark_Following").child(profileId)
                        .setValue(true)
                }

                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Bookmark_Follow").child(profileId)
                        .child("Bookmark_Followers").child(it1.toString())
                        .setValue(true)
                }
                Toast.makeText(this,"Bookmarked",Toast.LENGTH_SHORT).show()
            }else{
                binding.tvIsfollowed.text = "no"
                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Bookmark_Follow").child(it1)
                        .child("Bookmark_Following").child(profileId)
                        .removeValue()
                }
                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Bookmark_Follow").child(profileId)
                        .child("Bookmark_Followers").child(it1)
                        .removeValue()
                }
            }
        }

    }
    private suspend fun readData1(){
        withContext(Main){
            val executionTime = measureTimeMillis {
                async{
                    println("debug: launching 1st job: ${Thread.currentThread().name}")
                    setupTags()
                }.await()
            }
            Log.e(TAG,"debug: job1 and job2 are complete. It took ${executionTime} ms")

        }
    }
    private fun setupEpisodeList(){

        episodeRef = FirebaseDatabase.getInstance().getReference("Episodes")
        var query = episodeRef.orderByChild("novelId").equalTo(novelId)
        query.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    episodeList.clear()
                    for (element in snapshot.children){
                        val episode = element.getValue(Episode::class.java)
                        episodeList.add(episode!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
    private fun setupNovelDetail(){
        novelId = intent.getStringExtra("novelId").toString()
        novelRef = FirebaseDatabase.getInstance().getReference("Novels")
        var query = novelRef.orderByChild("novelId").equalTo(novelId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(element in snapshot.children) {
                    val novel = element.getValue(Novel::class.java)
                    val uri = novel?.getImageUri()
                    if (!uri.isNullOrBlank()) {
                        Picasso.get().load(uri.toString()).placeholder(R.drawable.superhero)
                            .into(binding.novelCoverIV)
                    }
                    if (novel != null) {
                        binding.novelTitleTV.text = novel.getTitle()
                        binding.expandTv.text = novel.getSynopsis()
//                        binding.novelAuthorTV.text = novel.getUid()
                        uid = novel.getUid()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private suspend fun setupTags(){
        withContext(Main){
            novelId = intent.getStringExtra("novelId").toString()
            tagRef = FirebaseDatabase.getInstance().getReference("Tags")
            var query = tagRef.orderByChild("novelId")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (element in snapshot.children) {
                        val tag = element.getValue(Tag::class.java)
                        if(tag!!.getNovelId()==novelId) {
                            tagList.add(tag)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            delay(500)
            tagAdapter = TagAdapter(this@FrontEndNovelActivity, tagList)
            binding.rvTags.adapter = tagAdapter
        }
    }
    private fun fetchAuthor(){
        author=""
        userRef = FirebaseDatabase.getInstance().getReference("Users")
        var query = userRef.orderByChild("uid")
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e(TAG,"Author count uid ${snapshot.childrenCount} $uid")
                for(element in snapshot.children){
                    val user = element.getValue(User::class.java)
                    if(user!!.getUid() == uid) {
                        author = user.getUsername()
                        binding.novelAuthorTV.text = author
                        return
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}