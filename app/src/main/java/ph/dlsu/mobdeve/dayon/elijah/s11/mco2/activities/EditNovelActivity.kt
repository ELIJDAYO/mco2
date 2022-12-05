package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelEditItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityEditNovelBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel

class EditNovelActivity : AppCompatActivity() {
    lateinit var binding:ActivityEditNovelBinding
    private lateinit var novelAdapter:NovelEditItemAdapter
    private lateinit var profileId: String
    private var novelList= arrayListOf<Novel>()
    lateinit var novelRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid
        binding.rvNovel.layoutManager = LinearLayoutManager(applicationContext)
        fetchNovel()
        novelAdapter = NovelEditItemAdapter(this,novelList)
        binding.rvNovel.adapter = novelAdapter

        binding.ibBack.setOnClickListener {
            val intent= Intent(this@EditNovelActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("last_fragment","userprofile")
            startActivity(intent)
            finish()
        }
        binding.btnWorkRepo.setOnClickListener {
            val intent = Intent(this, WorkRepoActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.ibAddNewNovel.setOnClickListener {
            val intent = Intent(this, CreateNewNovelActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun fetchNovel(){
        novelRef = FirebaseDatabase.getInstance().getReference("Novels")
        var query = novelRef.orderByChild("uid").equalTo(profileId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    novelList.clear()
                    for(element in snapshot.children){
                        val novel = element.getValue(Novel::class.java)
                        novelList.add(novel!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}