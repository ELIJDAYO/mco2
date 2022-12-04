package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelEditItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityEditNovelBinding

class EditNovelActivity : AppCompatActivity() {
    lateinit var binding:ActivityEditNovelBinding
    private lateinit var novelAdapter:NovelEditItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvNovel.layoutManager = LinearLayoutManager(applicationContext)
        novelAdapter = NovelEditItemAdapter()
        binding.rvNovel.adapter = novelAdapter

        binding.ibBack.setOnClickListener {
            val intent= Intent(this@EditNovelActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("last_fragment","userprofile")
            startActivity(intent)
            finish()
        }
        binding.btnWorkRepo.setOnClickListener {
            val intent = Intent(this, WorkRepoActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.ibAddNewNovel.setOnClickListener {
            val intent = Intent(this, CreateNewNovelActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}