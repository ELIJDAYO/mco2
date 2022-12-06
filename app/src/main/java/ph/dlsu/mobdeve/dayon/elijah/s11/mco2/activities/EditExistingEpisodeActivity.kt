package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityCreateNewEpisodeBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityCreateNewNovelBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import java.util.*
import kotlin.collections.HashMap

class EditExistingEpisodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewEpisodeBinding
    private lateinit var episodeId: String
    private lateinit var firebaseUser: FirebaseUser
    lateinit var episodeRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewEpisodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        episodeId = intent.getStringExtra("episodeId").toString()
        fetchEpisode()
        binding.ibBack.setOnClickListener {
            val intent= Intent(this@EditExistingEpisodeActivity,FrontEndEditNovelActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSaveToWorkRepo.setOnClickListener {
            addEpisodeInfo()
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
                        binding.NewEpisodeTitle.setText(episode!!.getEpisodeTitle())
                        binding.NewEpisodeContent.setText(episode.getContent())
                        return
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun addEpisodeInfo(){
        val epTitle = binding.NewEpisodeTitle.text.toString()
        val content = binding.NewEpisodeContent.text.toString()

        if (epTitle.isBlank() || content.isBlank()){
            Toast.makeText(this,"Either field is blank", Toast.LENGTH_SHORT).show()
            return
        }else{
            val episodeRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Episodes")
            val episodeMap = HashMap<String,Any>()

            episodeMap["episodeTitle"] = epTitle
            episodeMap["content"] = content
            episodeMap["isDraft"] = true
            episodeMap["isPublished"] = false

            episodeRef.child(episodeId).updateChildren(episodeMap)

            val intent = Intent(this, WorkRepoActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}