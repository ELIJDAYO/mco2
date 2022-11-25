package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityOtherUserBinding

class OtherUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtherUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}