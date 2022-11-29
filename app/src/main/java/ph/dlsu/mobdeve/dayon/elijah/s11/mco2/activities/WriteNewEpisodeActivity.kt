package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityWriteNewEpisodeBinding

class WriteNewEpisodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteNewEpisodeBinding
    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteNewEpisodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibBack.setOnClickListener {
            val intent= Intent(this@WriteNewEpisodeActivity,CreateNewNovelActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSaveToWorkRepo.setOnClickListener {
            addEpisodeInfo()
//            val intent = Intent(this, WorkRepoActivity::class.java)
//            startActivity(intent)
//            finish()
        }

    }
    private fun addEpisodeInfo(){
        val title = binding.NewEpisodeTitle.text.toString()
        val content = binding.NewEpisodeContent.text.toString()

        if (title.isBlank() || content.isBlank()){
            Toast.makeText(this,"Either field is blank", Toast.LENGTH_SHORT).show()
            return
        }else{

        }

    }
}