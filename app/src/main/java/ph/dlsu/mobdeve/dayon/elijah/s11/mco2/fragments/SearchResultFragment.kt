package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchResultNavAdapter
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

    private var resultList= arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchResultBinding.inflate(layoutInflater)





    }

    private fun fetchFilterFirebase(){
        dbSeries = FirebaseDatabase.getInstance().getReference("Series")

        var querySeriesPopularity = dbSeries.orderByChild("Popularity")
        var querySeriesRecent = dbSeries.orderByChild("Recent")
        var querySeriesOngoing = dbSeries.orderByChild("Ongoing")
        var querySeriesFinished = dbSeries.orderByChild("Finished")

        dbNovel = FirebaseDatabase.getInstance().getReference("Novel")

        var queryNovelPopularity = dbNovel.orderByChild("Popularity")
        var queryNovelRecent = dbNovel.orderByChild("Recent")
        var queryNovelOngoing = dbNovel.orderByChild("Ongoing")
        var queryNovelFinished = dbNovel.orderByChild("Finished")

        dbEssay = FirebaseDatabase.getInstance().getReference("Essay")

        var queryEssayPopularity = dbEssay.orderByChild("Popularity")
        var queryEssayRecent = dbEssay.orderByChild("Recent")
        var queryEssayOngoing = dbEssay.orderByChild("Ongoing")
        var queryEssayFinished = dbEssay.orderByChild("Finished")

        dbNSFW = FirebaseDatabase.getInstance().getReference("NSFW")

        var queryNSFWPopularity = dbNSFW.orderByChild("Popularity")
        var queryNSFWRecent = dbNSFW.orderByChild("Recent")
        var queryNSFWOngoing = dbNSFW.orderByChild("Ongoing")
        var queryNSFWFinished = dbNSFW.orderByChild("Finished")

        //change depends on the button clicked
        querySeriesPopularity.addValueEventListener(object : ValueEventListener{
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