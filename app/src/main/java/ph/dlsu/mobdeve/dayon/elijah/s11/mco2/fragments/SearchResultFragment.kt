package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
import java.util.*


class SearchResultFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<SearchResultNavAdapter.ViewHolder>? = null
    private lateinit var binding: FragmentSearchResultBinding

    private lateinit var dbSeries: DatabaseReference
    private lateinit var dbNovel: DatabaseReference
    private lateinit var dbEssay: DatabaseReference
    private lateinit var dbNSFW: DatabaseReference

    private val appContext = activity
    private var profileId: String = FirebaseAuth.getInstance().currentUser!!.uid
    private var resultList = arrayListOf<String>()


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

    private fun fetchFilterFirebase() {
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

        var noFilter = true



        if (filterSeries) {
            database = FirebaseDatabase.getInstance().getReference("Series")
            noFilter = false
        } else if (filterNovel) {
            database = FirebaseDatabase.getInstance().getReference("Novel")
            noFilter = false
        } else if (filterEssay) {
            database = FirebaseDatabase.getInstance().getReference("Essay")
            noFilter = false
        } else if (filterNSFW) {
            database = FirebaseDatabase.getInstance().getReference("NSFW")
            noFilter = false
        } else{
            database = FirebaseDatabase.getInstance().getReference("")
            noFilter = true

        }


        if (filterPopularity) {
            queryFirst = database.orderByChild("Stars")
        } else if (filterRecent) {
            queryFirst = database.orderByChild("releaseDateTime")
        } else {
            queryFirst = database
        }

        if (filterOngoing) {
            querySecond = queryFirst.orderByChild("Ongoing").equalTo("Ongoing")
        } else if (filterFinished) {
            querySecond = queryFirst.orderByChild("Finished").equalTo("Finished")
        } else {
            querySecond = queryFirst
        }



        querySecond.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                resultList.clear()

                if (filterPopularity) {
                    if (snapshot.exists()) {
                        for (element in snapshot.children) {
                            val result = element.getValue(String::class.java)
                            if (result != null) {
                                if (result.toInt() > 3) {
                                    resultList.add(result)
                                }
                            }
                        }
                    }
                    adapter = SearchResultNavAdapter(resultList)
                    binding.searchResultRV.adapter = adapter
                }
                else if(filterRecent){
                    if (snapshot.exists()) {
                        for (element in snapshot.children) {
                            val result = element.getValue(String::class.java)
                            if (result != null) {
                                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                val resultDate = inputFormat.parse(result)

                                val today = Date()
                                val calendar = Calendar.getInstance()
                                calendar.time = today

                                calendar.add(Calendar.DATE, 3)
                                val datePlusThreeDays = calendar.time

                                if (resultDate.before(datePlusThreeDays)){
                                    resultList.add(result)
                                }
                            }
                        }
                    }
                    adapter = SearchResultNavAdapter(resultList)
                    binding.searchResultRV.adapter = adapter
                }
                else if(noFilter){

                }
                else{
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
            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}