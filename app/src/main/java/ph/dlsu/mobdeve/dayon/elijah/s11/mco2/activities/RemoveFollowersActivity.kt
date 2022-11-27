package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.RemoveFollowersAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityRemoveFollowersBinding

class RemoveFollowersActivity : AppCompatActivity() {

    private var followerList:ArrayList<String> = ArrayList<String>()
    private lateinit var binding: ActivityRemoveFollowersBinding
    private lateinit var removeFollowersAdapter: RemoveFollowersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoveFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        followerList.add("User 1")
        followerList.add("User 2")
        followerList.add("User 3")

        binding.removeFollowerRV.layoutManager = LinearLayoutManager(applicationContext)
        removeFollowersAdapter = RemoveFollowersAdapter(applicationContext, followerList,"view")
        binding.removeFollowerRV.adapter = removeFollowersAdapter
    }
}