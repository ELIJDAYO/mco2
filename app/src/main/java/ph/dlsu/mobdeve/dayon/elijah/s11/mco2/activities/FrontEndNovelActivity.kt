package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelEpisodeAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityFrontEndNovelBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.User


class FrontEndNovelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFrontEndNovelBinding
    private lateinit var novelEpisodeAdapter: NovelEpisodeAdapter
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var profileId: String
    private lateinit var novelId:String
    lateinit var novelRef: DatabaseReference
    lateinit var episodeRef: DatabaseReference
    private  var episodeList= arrayListOf<Episode>()

    private  var uid = ""
    private lateinit var author:String
    lateinit var userRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrontEndNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvEpisodes.layoutManager = LinearLayoutManager(applicationContext)
        novelEpisodeAdapter = NovelEpisodeAdapter(applicationContext, episodeList,"view")
        binding.rvEpisodes.adapter = novelEpisodeAdapter
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        this.profileId = firebaseUser.uid

        setupNovelDetail()
        setupEpisodeList()
        fetchAuthor()
        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.ibNovel.visibility = View.INVISIBLE
        binding.ibEps.visibility = View.INVISIBLE
        binding.ivClose.setOnClickListener {
            val intent= Intent(this@FrontEndNovelActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("last_fragment","home")
            startActivity(intent)
            finish()
        }
        binding.shareTWIB.setOnClickListener {
            val authorName = binding.novelAuthorTV
            val novelName = binding.novelTitleTV
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Go Check out ${authorName}'s Novel ${novelName} at FICTION HERO App\nIt is available at App/Google Store")
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent,"Please select app: "))
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