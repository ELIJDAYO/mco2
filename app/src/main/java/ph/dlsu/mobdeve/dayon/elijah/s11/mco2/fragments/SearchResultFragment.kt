package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NotificationItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchResultNavAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentNotificationBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentSearchResultBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode


class SearchResultFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<SearchResultNavAdapter.ViewHolder>? = null
    private lateinit var binding: FragmentSearchResultBinding

    private lateinit var dbSeries: DatabaseReference
    private lateinit var dbNovel: DatabaseReference
    private lateinit var dbEssay: DatabaseReference
    private lateinit var dbNSFW: DatabaseReference

    private val appContext = requireContext().applicationContext
    private var profileId: String = FirebaseAuth.getInstance().currentUser!!.uid
    private var resultList= arrayListOf<String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentSearchResultBinding.inflate(inflater, container, false)



        binding.searchResultRV.layoutManager = LinearLayoutManager(appContext)

        fetchFilterFirebase()

        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        binding.searchResultRV.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = SearchResultNavAdapter(resultList)
        }
    }

    private fun fetchFilterFirebase(){
        lateinit var database: DatabaseReference
        lateinit var queryFirst: Query
        lateinit var querySecond: Query

        var filterPopularity: Boolean = false
        var filterRecent: Boolean = false

        var filterOngoing: Boolean = false
        var filterFinished: Boolean = false

        var filterSeries: Boolean = false
        var filterNovel: Boolean = false
        var filterEssay: Boolean = false
        var filterNSFW: Boolean = false



        if(filterSeries){
            database = FirebaseDatabase.getInstance().getReference("Series")
        }
        else if(filterNovel){
            database = FirebaseDatabase.getInstance().getReference("Novel")
        }
        else if(filterEssay){
            database = FirebaseDatabase.getInstance().getReference("Essay")
        }
        else if(filterNSFW){
            database = FirebaseDatabase.getInstance().getReference("NSFW")
        }

        if(filterPopularity){
            queryFirst = database.orderByChild("Popularity").equalTo("Popularity")
        }
        else if(filterRecent){
            queryFirst = database.orderByChild("Recent").equalTo("Recent")
        }
        else{
            queryFirst = database
        }

        if(filterOngoing){
            querySecond = queryFirst.orderByChild("Ongoing").equalTo("Ongoing")
        }
        else if(filterFinished){
            querySecond = queryFirst.orderByChild("Finished").equalTo("Finished")
        }
        else{
            querySecond = queryFirst
        }



        querySecond.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                resultList.clear()
                if (snapshot.exists()) {
                    for (element in snapshot.children) {
                        val result = element.getValue(String::class.java)
                        if (result != null) {
                            resultList.add(result)
                        }
                    }

                    adapter = SearchResultNavAdapter(resultList)
                    binding.searchResultRV.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}