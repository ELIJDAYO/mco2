package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityEpisodeViewerBinding

class EpisodeViewer : AppCompatActivity() {
    private lateinit var binding: ActivityEpisodeViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodeViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}