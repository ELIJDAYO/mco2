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
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.Interface.FirebaseCallback
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentHomeBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Tag
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        binding.rvNewest.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding.rvTag1.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)

        publishEpisodes()
        readData1(object : FirebaseCallback {
            override fun onCallBack1(list: ArrayList<String>) {
                Log.d(TAG, "Read Data1: On CallBack1 ${list.toString()}")
                novelIdList = list
            }

            override fun onCallBack2(list: ArrayList<String>) {
                Log.d(TAG, "Read Data1: On CallBack2 ${list.toString()}")
                novelDateUpdatedList = list
            }

            override fun onCallBack3(list: ArrayList<Novel>) {
                novelList = list
                for (mem in list){
                    Log.d(TAG, "Read Data1: On CallBack3 ${mem.getTitle()}")
                }
                adapter1 = context?.let { NovelItemAdapter(it,novelList as ArrayList<Novel>,novelDateUpdatedList as ArrayList<String>) }!!
                binding.rvNewest.adapter = adapter1
            }

        })
//        readData2(object :FirebaseCallback{
//            override fun onCallBack1(list: ArrayList<String>) {
//                tagNameList = list
//                Log.d(TAG, "Read Data 2: On CallBack1 ${list.toString()}")
//            }
//
//            override fun onCallBack2(list: ArrayList<String>) {
//                novelIdsByTag1 = list
//                Log.d(TAG, "Read Data 2: On CallBack2 ${list.toString()}")
//            }
//
//            override fun onCallBack3(list: ArrayList<Novel>) {
//                novelsByTag1 = list
//                for (mem in list){
//                    Log.d(TAG, "Read Data2: On CallBack3 ${mem.getTitle()}")
//    //                    novelList = list
//                }
//                adapter2 = context?.let { NovelItemAdapter(it,novelsByTag1,novelDateUpdatedList ) }!!
//                binding.rvTag1.adapter = adapter2
//            }
//
//        })


    }
    private fun fetchTop3Tags(firebaseCallBack: FirebaseCallback){
        tagRef = FirebaseDatabase.getInstance().getReference("Tags")
        var query = tagRef.orderByChild("novelId").limitToFirst(3)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    tagNameList.clear()
                    for (element in snapshot.children){
                        val tag = element.getValue(Tag::class.java)
                        if(tag!!.getTagName() !in tagNameList) {
                            tagNameList.add(tag!!.getTagName())
                        }
                    }
                    firebaseCallBack.onCallBack1(tagNameList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun fetchNovelIdsForTag1(firebaseCallBack: FirebaseCallback) {
        if(tagNameList.size < 1){
            return
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
//                    firebaseCallBack.onCallBack1(novelDateUpdatedList)
                    firebaseCallBack.onCallBack2(novelIdsByTag1)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun fetchNovelsForTag1(firebaseCallBack: FirebaseCallback){
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
                            Log.e(TAG,"fetchNovelsForTag1 ${novel.getTitle()}")
                        }
                    }
                    firebaseCallBack.onCallBack3(novelsByTag1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun readData1(firebaseCallBack: FirebaseCallback) {
        fetchRecentlyUpdatedNovelId(firebaseCallBack)
        fetchRecentlyUpdatedNovel(firebaseCallBack)
    }
    private fun readData2(firebaseCallBack: FirebaseCallback){
        fetchTop3Tags(firebaseCallBack)
        fetchNovelIdsForTag1(firebaseCallBack)
        fetchNovelsForTag1(firebaseCallBack)
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
    private fun fetchRecentlyUpdatedNovelId(firebaseCallBack: FirebaseCallback){
        episodeRef = FirebaseDatabase.getInstance().getReference("Episodes")
        var query = episodeRef.orderByChild("releaseDateTime").limitToFirst(10)
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    novelIdList.clear()
                    novelDateUpdatedList.clear()
                    for(element in snapshot.children){
                        val episode = element.getValue(Episode::class.java)
                        if(episode!!.getNovelId() !in novelIdList && episode.getIsPublished()){
                            novelIdList.add(episode.getNovelId())
                            novelDateUpdatedList.add(episode.getReleaseDateTime())
                        }
                    }
//                    Log.v("Async101", "End of onDataChange novelIdListSize ${novelIdList.size}")
                    firebaseCallBack.onCallBack1(novelIdList)
                    firebaseCallBack.onCallBack2(novelDateUpdatedList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "$error")
            }
        })
    }
    private fun fetchRecentlyUpdatedNovel(firebaseCallBack: FirebaseCallback){
        novelRef = FirebaseDatabase.getInstance().getReference("Novels")
        var query = novelRef.orderByChild("novelId")
        query.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                novelList.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var novel = element.getValue(Novel::class.java)
                        if(novel!!.getNovelId() in novelIdList) {
                            novelList.add(novel)
                        }
                    }
                    firebaseCallBack.onCallBack3(novelList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented") }
        })

    }

}