package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelChapterAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityFrontEndNovelBinding


class FrontEndNovel : AppCompatActivity() {

    private var chapterTitle:ArrayList<String> = ArrayList<String>()
    private lateinit var binding: ActivityFrontEndNovelBinding
    private lateinit var novelChapterAdapter: NovelChapterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrontEndNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chapterTitle = resources.getStringArray(R.array.novelChapterTitle).toCollection(ArrayList())

        binding.novelChapBTN.setLayoutManager(LinearLayoutManager(applicationContext))

        novelChapterAdapter = NovelChapterAdapter(applicationContext, chapterTitle)
        binding.novelChapBTN.setAdapter(novelChapterAdapter)

    }

}