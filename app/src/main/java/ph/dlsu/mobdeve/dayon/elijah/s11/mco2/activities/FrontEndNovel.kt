package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityFrontEndNovelBinding


class FrontEndNovel : AppCompatActivity() {
    private lateinit var binding: ActivityFrontEndNovelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrontEndNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}