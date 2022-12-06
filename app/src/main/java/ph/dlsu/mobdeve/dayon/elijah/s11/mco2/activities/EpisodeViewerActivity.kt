package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityEpisodeViewerBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode

class EpisodeViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEpisodeViewerBinding
    lateinit var episodeRef: DatabaseReference
    private lateinit var episodeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodeViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        episodeId = intent.getStringExtra("episodeId").toString()
        fetchEpisode()
        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.ivClose.setOnClickListener {
            val intent= Intent(this@EpisodeViewerActivity,FrontEndNovelActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.putExtra("last_fragment","home")
            startActivity(intent)
            finish()
        }
        binding.novelNextBTN.setOnClickListener {
            val intent= Intent(this@EpisodeViewerActivity,EpisodeViewerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun fetchEpisode(){
        episodeRef = FirebaseDatabase.getInstance().getReference("Episodes")
        var query = episodeRef.orderByChild("episodeId").equalTo(episodeId)
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (element in snapshot.children){
                        val episode = element.getValue(Episode::class.java)
                        binding.novelChapTitle.text = episode!!.getEpisodeTitle()
                        binding.novelContentTV.text = episode.getContent()
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