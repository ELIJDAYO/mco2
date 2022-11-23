package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.WorkRepoItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityEditNovelBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityWorkRepoBinding

class WorkRepoActivity : AppCompatActivity() {
    lateinit var binding: ActivityWorkRepoBinding
    private lateinit var workRepoAdapter: WorkRepoItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvWorkRepo.layoutManager = LinearLayoutManager(applicationContext)
        workRepoAdapter = WorkRepoItemAdapter()
        binding.rvWorkRepo.adapter = workRepoAdapter
        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}