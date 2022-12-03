package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NotificationItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchHistoryAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchResultAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchResultNavAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentNotificationBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentSearchBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {

    private var adapter: RecyclerView.Adapter<SearchResultNavAdapter.ViewHolder>? = null
    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var database: DatabaseReference
    private var searchResultNavArray = arrayListOf<String>()
    private var searchResultNavStarArray = arrayListOf<Int>()
    private lateinit var profileId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid

        binding = FragmentSearchResultBinding.inflate(layoutInflater)

        fetchSearchResultFirebase()
    }

    private fun fetchSearchResultFirebase(){
        database = FirebaseDatabase.getInstance().getReference("Novel")
        var query = database.orderByChild("title")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                searchResultNavArray.clear()
                searchResultNavStarArray.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var searchResult = element.getValue(String::class.java)
                        searchResultNavArray.add(searchResult!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        var query2 = database.orderByChild("star")
        query2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var searchResult = element.getValue(Int::class.java)
                        searchResultNavStarArray.add(searchResult!!)
                    }
                    adapter = SearchResultNavAdapter(searchResultNavArray, searchResultNavStarArray)
                    binding.searchResultRV.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}