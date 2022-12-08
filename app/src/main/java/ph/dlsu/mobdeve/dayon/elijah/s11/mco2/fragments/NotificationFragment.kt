package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NotificationItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.WorkRepoItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentNotificationBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Notification


class NotificationFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: RecyclerView.Adapter<NotificationItemAdapter.ViewHolder>
    private lateinit var binding: FragmentNotificationBinding
    private var profileId: String = FirebaseAuth.getInstance().currentUser!!.uid
    private var notificationArray= arrayListOf<Notification>()
    private val appContext = requireContext().applicationContext
    private lateinit var notificationRef: DatabaseReference




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentNotificationBinding.inflate(inflater, container, false)



        binding.notificationItemBTN.layoutManager = LinearLayoutManager(appContext)

        fetchNotificationFirebase()

        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        binding.notificationItemBTN.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = NotificationItemAdapter(notificationArray)
        }
    }


    private fun fetchNotificationFirebase(){
        notificationRef = FirebaseDatabase.getInstance().getReference("Notification")
        var query = notificationRef.orderByChild("receiverId").equalTo(profileId)
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                notificationArray.clear()
                if(snapshot.exists()){
                    for(element in snapshot.children){
                        var notification = element.getValue(Notification::class.java)

                        if (notification != null) {
                            notificationArray.add(notification)
                        }

                    }
                    adapter = NotificationItemAdapter(notificationArray)
                    binding.notificationItemBTN.adapter = adapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

}