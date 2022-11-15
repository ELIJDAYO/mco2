package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityWriteNewEpisodeBinding

class WriteNewEpisode : AppCompatActivity() {

    private lateinit var binding: ActivityWriteNewEpisodeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteNewEpisodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}