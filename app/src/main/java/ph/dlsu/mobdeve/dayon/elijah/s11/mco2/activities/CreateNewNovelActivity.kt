package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityCreateNewNovelBinding

class CreateNewNovelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewNovelBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}