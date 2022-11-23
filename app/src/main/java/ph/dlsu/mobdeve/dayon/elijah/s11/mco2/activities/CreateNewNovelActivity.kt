package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityCreateNewNovelBinding

class CreateNewNovelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewNovelBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ibBack.setOnClickListener {
            val intent= Intent(this@CreateNewNovelActivity,EditNovelActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnCreateEpisode.setOnClickListener {
            val intent = Intent(this, WriteNewEpisodeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}