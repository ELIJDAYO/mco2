package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditFrontEndNovelActivity



class NovelEditItemAdapter : RecyclerView.Adapter<NovelEditItemAdapter.ViewHolder>{
//    private val titleNovel = arrayOf(
//        "Battle Ship Yamato Awakens",
//        "Dungeon Seeker",
//        "Helmed Beast Slayer",
//        "High School of the Elites")
    private var titleNovel: ArrayList<String>
    private lateinit var context: Context

    constructor(context: Context, titleNovel: ArrayList<String>){
        this.context = context
        this.titleNovel = titleNovel
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView? = null

        init {
            itemTitle = itemView.findViewById(R.id.tv_title_inp)

            itemView.setOnClickListener {
                var position: Int = bindingAdapterPosition
                val context = itemView.context
//              val intent = Intent(context, HomeFragment::class.java).apply {
//                    putExtra("NUMBER", position)

                    if (position >= 0){
                        itemView.setOnClickListener{
                            val intent = Intent(context, EditFrontEndNovelActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
//                context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_novel_edit_post, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle?.text = titleNovel[i]
    }

    override fun getItemCount(): Int {
        return titleNovel.size
    }
}