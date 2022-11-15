package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityEpisodeViewerBinding

class EpisodeViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEpisodeViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodeViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}