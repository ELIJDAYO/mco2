package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.Interface.FirebaseCallback
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentHomeBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Tag
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

class HomeFragment : Fragment() {
    private lateinit var adapter1: NovelItemAdapter
    private lateinit var adapter2: NovelItemAdapter

    private var _binding: FragmentHomeBinding? = null
    lateinit var episodeRef: DatabaseReference
    lateinit var novelRef: DatabaseReference
    lateinit var tagRef: DatabaseReference

    //getting the recently updated novel
    private var novelIdList= arrayListOf<String>()
    private var novelDateUpdatedList= arrayListOf<String>()
    private var novelList= arrayListOf<Novel>()
//getting top3 tags by childCounts and get novel
    //reuse novelId and novelList
    private var tagNameList = arrayListOf<String>()
    private var novelIdsByTag1 = arrayListOf<String>()
    private var novelsByTag1 = arrayListOf<Novel>()
    private var novelIdsByTag2 = arrayListOf<String>()
    private var novelIdsByTag3 = arrayListOf<String>()


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        binding.rvNewest.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding.rvTag1.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)

        publishEpisodes()


        CoroutineScope(IO).launch{
            readData1()
            readData2()
        }

    }
    private suspend fun readData1(){
        withContext(IO){
            val executionTime = measureTimeMillis {
                async {
                    println("debug: launching 1st job: ${Thread.currentThread().name}")
                    fetchRecentlyUpdatedNovelId()
                }.await()

                async {
                    println("debug: launching 2nd job: ${Thread.currentThread().name}")
                    fetchRecentlyUpdatedNovel()
                }.await()

            }
            Log.e(TAG,"debug: job1 and job2 are complete. It took ${executionTime} ms")

        }    }
    private suspend fun readData2() {
        withContext(IO){
            val executionTime = measureTimeMillis {
                async {
                    println("debug: launching 1st job: ${Thread.currentThread().name}")
                    fetchTop3Tags()
                }.await()

                async {
                    println("debug: launching 2nd job: ${Thread.currentThread().name}")
                    fetchNovelIdsForTag1()
                }.await()

                async {
                    println("debug: launching 3rd job: ${Thread.currentThread().name}")
                    fetchNovelsForTag1()
                }.await()
            }
            Log.e(TAG,"debug: job1 and job2 are complete. It took ${executionTime} ms")

        }
    }
    private suspend fun fetchTop3Tags(){
        withContext(Main){
            tagRef = FirebaseDatabase.getInstance().getReference("Tags")
            var query = tagRef.orderByChild("novelId").limitToFirst(3)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        tagNameList.clear()
                        for (element in snapshot.children){
                            val tag = element.getValue(Tag::class.java)
                            if(tag!!.getTagName() !in tagNameList) {
                                tagNameList.add(tag.getTagName())
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            delay(500)
        }
    }
    private suspend fun fetchNovelIdsForTag1() {

        withContext(Main){
            if(tagNameList.size < 1){
                return@withContext
            }
            tagRef = FirebaseDatabase.getInstance().getReference("Tags")
            var query = tagRef.orderByChild("tagName").equalTo(tagNameList[0]).limitToLast(10)
            query.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    novelIdsByTag1.clear()
                    novelDateUpdatedList.clear()
                    if(snapshot.exists()){
                        for(element in snapshot.children) {
                            val tag = element.getValue(Tag::class.java)
                            novelIdsByTag1.add(tag!!.getNovelId())
                            novelDateUpdatedList.add("")
                        }
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            delay(500)
            Log.e(TAG,"Size of NovelIdsByTag: ${novelIdsByTag1[0]}")
        }
    }
    private suspend fun fetchNovelsForTag1(){
        withContext(Main){
            novelRef = FirebaseDatabase.getInstance().getReference("Novels")
            var query = novelRef.orderByChild("novelId")
            query.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    novelsByTag1.clear()
                    if(snapshot.exists()){
                        for (element in snapshot.children){
                            val novel = element.getValue(Novel::class.java)
                            if(novel!!.getNovelId() in novelIdsByTag1) {
                                novelsByTag1.add(novel)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            delay(1000)
            adapter2 = context?.let { NovelItemAdapter(it,novelsByTag1,novelDateUpdatedList ) }!!
            binding.rvTag1.adapter = adapter2
        }
    }

    private fun publishEpisodes(){
        episodeRef = FirebaseDatabase.getInstance().getReference("Episodes")
        var query = episodeRef.orderByChild("isPublished").equalTo(false)
        query.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var episode = element.getValue(Episode::class.java)
                        if(compareNowAndReleaseDate(episode!!.getReleaseDateTime())){
                            val episodeMap = HashMap<String,Any>()
                            episodeMap["episodeId"] = episode.getEpisodeId()
                            episodeMap["isDraft"] = false
                            episodeMap["isPublished"] = true
                            episodeRef.child(episode.getEpisodeId()).updateChildren(episodeMap)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        } )

    }
    private fun compareNowAndReleaseDate(releaseDateTime:String): Boolean {
        val now = Calendar.getInstance()
        val year = now[Calendar.YEAR]
        val month = now[Calendar.MONTH] + 1 // Note: zero based!
        val day = now[Calendar.DAY_OF_MONTH]
        val hour = now[Calendar.HOUR_OF_DAY]
        val minute = now[Calendar.MINUTE]
        val dateNow = "$year-$month-$day $hour:$minute"

        val formatter = SimpleDateFormat("yyyy-mm-dd hh:mm")
        val current = formatter.parse(dateNow)
        val target = formatter.parse(releaseDateTime)
        Log.d("TAG","$current vs $target")

        val cmp = current?.compareTo(target)
        var result = false
        if (cmp != null) {
            when{
                cmp >= 0 -> {result = true}
            }
        }
        return result
    }
    private suspend fun fetchRecentlyUpdatedNovelId(){
        withContext(Main) {
            episodeRef = FirebaseDatabase.getInstance().getReference("Episodes")
            var query = episodeRef.orderByChild("releaseDateTime").limitToFirst(10)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        novelIdList.clear()
                        novelDateUpdatedList.clear()
                        for (element in snapshot.children) {
                            val episode = element.getValue(Episode::class.java)
                            if (episode!!.getNovelId() !in novelIdList && episode.getIsPublished()) {
                                novelIdList.add(episode.getNovelId())
                                novelDateUpdatedList.add(episode.getReleaseDateTime())
                            }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "$error")
                }
            })
            delay(500)
        }
    }
    private suspend fun fetchRecentlyUpdatedNovel(){
        withContext(Main) {
            novelRef = FirebaseDatabase.getInstance().getReference("Novels")
            var query = novelRef.orderByChild("novelId")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    novelList.clear()
                    if (snapshot.exists()) {
                        for (element in snapshot.children) {
                            var novel = element.getValue(Novel::class.java)
                            if (novel!!.getNovelId() in novelIdList) {
                                novelList.add(novel)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            delay(500)
            adapter1 = context?.let { NovelItemAdapter(it,novelList,novelDateUpdatedList) }!!
            binding.rvNewest.adapter = adapter1
        }
    }

}