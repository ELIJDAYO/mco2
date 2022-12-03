package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelEditItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.RemoveBookmarkAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityRemoveBookmarkBinding

class RemoveBookmarkActivity : AppCompatActivity() {

    private var bookmarkList:ArrayList<String> = ArrayList<String>()
    private lateinit var binding: ActivityRemoveBookmarkBinding
    private lateinit var removeBookmarkAdapter: RemoveBookmarkAdapter
    private lateinit var profileId: String
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoveBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid

        binding.removeBookmarkRV.layoutManager = LinearLayoutManager(applicationContext)

        fetchBookmarkFirebase()

    }

    private fun fetchBookmarkFirebase(){
        database = FirebaseDatabase.getInstance().getReference("Bookmark")
        var query = database.orderByChild("title")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookmarkList.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var bookmark = element.getValue(String::class.java)
                        bookmarkList.add(bookmark!!)
                    }
                    removeBookmarkAdapter = RemoveBookmarkAdapter(this@RemoveBookmarkActivity, bookmarkList, "view")
                    binding.removeBookmarkRV.adapter = removeBookmarkAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}