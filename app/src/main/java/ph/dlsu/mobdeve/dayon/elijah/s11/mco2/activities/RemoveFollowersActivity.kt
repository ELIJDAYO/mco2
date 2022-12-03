package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.RemoveBookmarkAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.RemoveFollowersAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityRemoveFollowersBinding

class RemoveFollowersActivity : AppCompatActivity() {

    private var followerList:ArrayList<String> = ArrayList<String>()
    private lateinit var binding: ActivityRemoveFollowersBinding
    private lateinit var removeFollowersAdapter: RemoveFollowersAdapter
    private lateinit var profileId: String
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoveFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid

        binding.removeFollowerRV.layoutManager = LinearLayoutManager(applicationContext)

        fetchFollowerFirebase()
    }

    private fun fetchFollowerFirebase(){
        database = FirebaseDatabase.getInstance().getReference("Bookmark")
        var query = database.orderByChild("title")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                followerList.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var follower = element.getValue(String::class.java)
                        followerList.add(follower!!)
                    }
                    removeFollowersAdapter = RemoveFollowersAdapter(this@RemoveFollowersActivity, followerList, "view")
                    binding.removeFollowerRV.adapter = removeFollowersAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}