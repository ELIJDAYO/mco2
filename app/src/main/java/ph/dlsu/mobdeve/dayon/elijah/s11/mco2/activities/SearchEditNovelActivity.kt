package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelEditItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchResultAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivitySearchEditNovelBinding

class SearchEditNovelActivity : AppCompatActivity() {

    private var searchResultList:ArrayList<String> = ArrayList<String>()
    private lateinit var binding: ActivitySearchEditNovelBinding
    private lateinit var searchResultAdapter: SearchResultAdapter
    private lateinit var profileId: String
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchEditNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid

        binding.searchResultList.layoutManager = LinearLayoutManager(applicationContext)

        fetchSearchResultFirebase()
    }

    private fun fetchSearchResultFirebase(){
        database = FirebaseDatabase.getInstance().getReference("Novel")
        var query = database.orderByChild("title")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                searchResultList.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var searchResult = element.getValue(String::class.java)
                        searchResultList.add(searchResult!!)
                    }
                    searchResultAdapter = SearchResultAdapter(this@SearchEditNovelActivity, searchResultList)
                    binding.searchResultList.adapter = searchResultAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}