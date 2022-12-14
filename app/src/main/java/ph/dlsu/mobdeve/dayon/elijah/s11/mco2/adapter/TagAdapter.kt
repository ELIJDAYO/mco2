package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Tag


class TagAdapter : RecyclerView.Adapter<TagAdapter.ViewHolder>{

    private var tagList= arrayListOf<Tag>()
    private var context:Context
    private lateinit var tagRemove:Tag

    constructor(context:Context,tagList:ArrayList<Tag>){
        this.context = context
        this.tagList = tagList
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTag: TextView? = null
        init {
            itemTag = itemView.findViewById(R.id.tv_title_it)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_tag, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val tag = tagList[i]
        viewHolder.itemTag?.text = tag.getTagName()

    }
    override fun getItemCount(): Int {
        return tagList.size
    }
}