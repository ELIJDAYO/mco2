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
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentNotificationBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {


    private var adapter: RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>? = null
    private lateinit var binding : FragmentSearchBinding
    private lateinit var database: DatabaseReference
    private var searchHistoryArray = arrayListOf<String>()
    private lateinit var profileId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid

        binding = FragmentSearchBinding.inflate(layoutInflater)

        fetchSearchHistoryFirebase()
    }

    private fun fetchSearchHistoryFirebase(){
        database = FirebaseDatabase.getInstance().getReference("SearchHistory")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                searchHistoryArray.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var searchHistory = element.getValue(String::class.java)
                        searchHistoryArray.add(searchHistory!!)
                    }
                    adapter = SearchHistoryAdapter(searchHistoryArray)
                    binding.searchHistoryList.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}