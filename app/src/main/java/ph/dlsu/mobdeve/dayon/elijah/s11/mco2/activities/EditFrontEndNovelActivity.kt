package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelEpisodeAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.TagAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityFrontEndNovelBinding


class EditFrontEndNovelActivity : AppCompatActivity() {

    private var chapterTitle:ArrayList<String> = ArrayList<String>()
    private lateinit var binding: ActivityFrontEndNovelBinding
    private lateinit var novelEpisodeAdapter: NovelEpisodeAdapter
    private lateinit var tagAdapter:TagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrontEndNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chapterTitle = resources.getStringArray(R.array.novelChapterTitle).toCollection(ArrayList())
        binding.rvEpisodes.layoutManager = LinearLayoutManager(applicationContext)
//        novelEpisodeAdapter = NovelEpisodeAdapter(applicationContext, chapterTitle, "edit")
        binding.rvEpisodes.adapter = novelEpisodeAdapter

        binding.rvTags.layoutManager = LinearLayoutManager(applicationContext)
//        tagAdapter = TagAdapter(this, )
        binding.rvTags.adapter = tagAdapter


        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
//            val intent= Intent(this@WriteNewEpisodeActivity,WorkRepoActivity::class.java)
//            startActivity(intent)
//            finish()
        }
        binding.ivClose.visibility = View.INVISIBLE
//        binding.ivClose.setOnClickListener {
//            val intent= Intent(this@EditFrontEndNovelActivity,MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.putExtra("last_fragment","home")
//            startActivity(intent)
//            finish()
//        }
    }
}