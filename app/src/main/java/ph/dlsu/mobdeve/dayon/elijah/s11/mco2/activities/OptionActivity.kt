package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityOptionBinding

class OptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.ibBack.setOnClickListener {
            val intent= Intent(this@OptionActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("last_fragment","userprofile")
            startActivity(intent)
            finish()
        }
    }

}