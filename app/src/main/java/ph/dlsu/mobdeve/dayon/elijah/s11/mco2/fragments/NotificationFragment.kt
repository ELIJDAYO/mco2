package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NotificationItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.WorkRepoItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentNotificationBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode


class NotificationFragment : Fragment() {

    private lateinit var adapter: RecyclerView.Adapter<NotificationItemAdapter.ViewHolder>
    private lateinit var binding : FragmentNotificationBinding
    private lateinit var database: DatabaseReference
    private var notificationList = arrayListOf<String>()
    private lateinit var profileId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid

        binding = FragmentNotificationBinding.inflate(layoutInflater)

        fetchNotificationFirebase()
    }

    private fun fetchNotificationFirebase(){
        database = FirebaseDatabase.getInstance().getReference("Notification")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notificationList.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var notification = element.getValue(String::class.java)
                        notificationList.add(notification!!)
                    }
                    adapter = NotificationItemAdapter(notificationList)
                    binding.notificationItemBTN.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}