package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
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
import kotlin.system.measureTimeMillis


class FrontEndEditNovelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFrontEndNovelBinding
    private lateinit var novelEpisodeAdapter: NovelEpisodeAdapter
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var novelId:String
    private lateinit var novelTitle:String

    lateinit var novelRef: DatabaseReference
    lateinit var episodeRef: DatabaseReference
    lateinit var tagRef: DatabaseReference
    private lateinit var tagAdapter: TagAdapter

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
        binding.tvPageName.text = "Edit Mode"
        binding.ivClose.visibility = View.INVISIBLE
        setupNovelDetail()
        setupEpisodeList()
        fetchAuthor()
        CoroutineScope(Main).launch{
            readData1()
        }
        novelEpisodeAdapter = NovelEpisodeAdapter(applicationContext, episodeList,"edit")
        binding.rvEpisodes.adapter = novelEpisodeAdapter
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.ivNovel.visibility = View.VISIBLE
        binding.ivEps.visibility = View.VISIBLE
        binding.ivAdd.visibility = View.VISIBLE
        binding.ivNovel.setOnClickListener{
            val intent= Intent(this@FrontEndEditNovelActivity,EditExistingNovelActivity::class.java)
            intent.putExtra("novelId", novelId)

            startActivity(intent)
            finish()
        }
        binding.ivAdd.setOnClickListener {
            val intent= Intent(this@FrontEndEditNovelActivity,CreateNewEpisodeActivity::class.java)
            intent.putExtra("novelId",novelId)
            intent.putExtra("novelTitle",novelTitle)
            Log.e(TAG,"FrontEndEditNovel: novelTitle $novelTitle")
            startActivity(intent)
            finish()
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
    private suspend fun readData1(){
        withContext(Main){
            val executionTime = measureTimeMillis {
                async{
                    println("debug: launching 1st job: ${Thread.currentThread().name}")
                    setupTags()
                }.await()
            }
            Log.e(ContentValues.TAG,"debug: job1 and job2 are complete. It took ${executionTime} ms")

        }
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
                        novelTitle = novel.getTitle()
                        binding.novelTitleTV.text = novelTitle

                        binding.expandTv.text = novel.getSynopsis()
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
        withContext(Main) {
            novelId = intent.getStringExtra("novelId").toString()

            tagRef = FirebaseDatabase.getInstance().getReference("Tags")
            var query = tagRef.orderByChild("novelId").equalTo(novelId)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e(TAG, "Tag count ${snapshot.childrenCount}")
                    for (element in snapshot.children) {
                        val tag = element.getValue(Tag::class.java)
                        tagList.add(tag!!)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            delay(300)
            tagAdapter = TagAdapter(this@FrontEndEditNovelActivity, tagList)
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