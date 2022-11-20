package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchResultAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivitySearchEditNovelBinding

class SearchEditNovel : AppCompatActivity() {

    private var searchResultList:ArrayList<String> = ArrayList<String>()
    private lateinit var binding: ActivitySearchEditNovelBinding
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchEditNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchResultAdapter = SearchResultAdapter(applicationContext, searchResultList)
        binding.searchResultList.adapter = searchResultAdapter
    }
}