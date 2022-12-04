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

class WorkRepoActivity : AppCompatActivity() {
    lateinit var binding: ActivityWorkRepoBinding
    private lateinit var workRepoAdapter: WorkRepoItemAdapter
    private lateinit var episodeRef: DatabaseReference
    private lateinit var profileId: String
    private var repoList= arrayListOf<Episode>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid

        binding.rvWorkRepo.layoutManager = LinearLayoutManager(applicationContext)
        fetchEpisodesFirebase()


        binding.ibBack.setOnClickListener {
            val intent = Intent(this, EditNovelActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun fetchEpisodesFirebase(){
        episodeRef = FirebaseDatabase.getInstance().getReference("Episodes")
        var query = episodeRef.orderByChild("novelId")
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                repoList.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var episode = element.getValue(Episode::class.java)
                        if(episode!!.getIsDraft() || !episode.getIsPublished()){
                            repoList.add(episode)
                        }
                    }
                    workRepoAdapter = WorkRepoItemAdapter(this@WorkRepoActivity,repoList)
                    binding.rvWorkRepo.adapter = workRepoAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}