package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.WorkRepoItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityWorkRepoBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel

class WorkRepoActivity : AppCompatActivity() {
    lateinit var binding: ActivityWorkRepoBinding
    private lateinit var workRepoAdapter: WorkRepoItemAdapter
    private lateinit var episodeRef: DatabaseReference
    private lateinit var profileId: String
    private var repoList= ArrayList<Episode>()

    private lateinit var novelTitle: String
    private var novelTitleList= ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid

        binding.rvWorkRepo.layoutManager = LinearLayoutManager(applicationContext)
        fetchEpisodesFirebase()
        workRepoAdapter = WorkRepoItemAdapter(this, novelTitleList,repoList)
        binding.rvWorkRepo.adapter = workRepoAdapter
        binding.ibBack.setOnClickListener {
            val intent = Intent(this, EditNovelActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun fetchEpisodesFirebase(){
        episodeRef = FirebaseDatabase.getInstance().reference.child("Episodes")
        var query = episodeRef.orderByChild("novelId")
            query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                repoList.clear()
                novelTitleList.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var episode = snapshot.getValue(Episode::class.java)
                        novelTitleList = ArrayList<String>()
                        if (episode != null) {
                            if(episode.getIsDraft()){
                                repoList.add(episode)
                                fetchNovelTitle(episode.getNovelId())
                                novelTitleList.add(novelTitle)
                            }
                        }
                    }
                    workRepoAdapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun fetchNovelTitle(novelId:String){
        val novelRef = FirebaseDatabase.getInstance().reference.child("Novel")
        var query = novelRef.orderByChild("novelId").equalTo(novelId)
        query.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val novel = snapshot.getValue(Novel::class.java)
                    if (novel != null) {
                        novelTitle = novel.getTitle()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}