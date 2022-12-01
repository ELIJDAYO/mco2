package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityWriteNewEpisodeBinding
import java.util.*
import kotlin.collections.HashMap

class CreateNewEpisodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteNewEpisodeBinding
    private var novelId: String? = null
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteNewEpisodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibBack.setOnClickListener {
            val intent= Intent(this@CreateNewEpisodeActivity,CreateNewNovelActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSaveToWorkRepo.setOnClickListener {
            addEpisodeInfo()
        }

    }
    private fun addEpisodeInfo(){
        val epTitle = binding.NewEpisodeTitle.text.toString()
        val content = binding.NewEpisodeContent.text.toString()

        if (epTitle.isBlank() || content.isBlank()){
            Toast.makeText(this,"Either field is blank", Toast.LENGTH_SHORT).show()
            return
        }else{
            novelId = intent.getStringExtra("novelId")
            val novelTitle =  intent.getStringExtra("novelTitle")
            val episodeRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Episodes")
            val episodeId = episodeRef.push().key.toString()
            val episodeMap = HashMap<String,Any>()

            episodeMap["episodeId"] = episodeId
            episodeMap["novelTitle"] = novelTitle!!
            episodeMap["novelId"] = novelId!!
            episodeMap["episodeTitle"] = epTitle
            episodeMap["content"] = content
            episodeMap["isDraft"] = true
            episodeMap["isPublished"] = false

            episodeRef.child(episodeId).setValue(episodeMap)

            val intent = Intent(this, WorkRepoActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}