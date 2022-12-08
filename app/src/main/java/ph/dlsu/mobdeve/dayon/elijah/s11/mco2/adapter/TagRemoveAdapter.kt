package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
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
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Tag
import kotlin.system.measureTimeMillis


class TagRemoveAdapter : RecyclerView.Adapter<TagRemoveAdapter.ViewHolder>{
    private var tagList= arrayListOf<String>()
    private lateinit var context:Context
    private var novelId: String


    constructor(context:Context,tagList:ArrayList<String>,novelId:String){
        this.context = context
        this.tagList = tagList
        this.novelId = novelId
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTag: TextView? = null
        var removeIv: ImageView? = null
        init {
            itemTag = itemView.findViewById(R.id.tv_title_irt)
            removeIv = itemView.findViewById(R.id.iv_remove_irt)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_remove_tag, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val tag = tagList[i]
        viewHolder.itemTag?.text = tag

        viewHolder.removeIv?.setOnClickListener{
            tagList.removeAt(i)
            notifyDataSetChanged()
        }
    }
    override fun getItemCount(): Int {
        return tagList.size
    }
}