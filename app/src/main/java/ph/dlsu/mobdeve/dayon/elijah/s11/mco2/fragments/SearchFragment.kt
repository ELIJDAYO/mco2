package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.content.Intent
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
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchResultNavAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentSearchBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Notification

class SearchFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: RecyclerView.Adapter<SearchResultNavAdapter.ViewHolder>
    private lateinit var binding: FragmentSearchBinding
    private var profileId: String = FirebaseAuth.getInstance().currentUser!!.uid
    private var searchResultArray= arrayListOf<Notification>()
    private val appContext = requireContext().applicationContext
    private lateinit var searchResultRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentSearchBinding.inflate(inflater, container, false)


        binding.searchButton.setOnClickListener {
            fetchSearchResultFirebase()


        }
        return binding.root
    }



    private fun fetchSearchResultFirebase(){
        searchResultRef = FirebaseDatabase.getInstance().getReference("Notification")
        var query = searchResultRef.orderByChild("receiverId").equalTo(profileId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                searchResultArray.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var searchResult = element.getValue(Notification::class.java)

                        if (searchResult != null) {
                            searchResultArray.add(searchResult)

                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
}