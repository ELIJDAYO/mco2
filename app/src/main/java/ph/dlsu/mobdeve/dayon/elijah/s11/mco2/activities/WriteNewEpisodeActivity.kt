package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityWriteNewEpisodeBinding

class WriteNewEpisodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteNewEpisodeBinding


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
            val intent = Intent(this, WorkRepoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}