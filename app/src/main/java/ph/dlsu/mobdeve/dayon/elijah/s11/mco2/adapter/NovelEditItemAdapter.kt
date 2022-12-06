package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditExistingEpisodeActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndEditNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel


class NovelEditItemAdapter : RecyclerView.Adapter<NovelEditItemAdapter.ViewHolder>{
    private var novelList:ArrayList<Novel>
    private var context: Context
    constructor(context: Context, novelList:ArrayList<Novel>){
        this.context = context
        this.novelList = novelList
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView? = null

        init {
            itemTitle = itemView.findViewById(R.id.tv_title_inp)
//
//            itemView.setOnClickListener {
//                var position: Int = bindingAdapterPosition
//                val context = itemView.context
//
//            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_novel_edit_post, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val novel = novelList[i]

        viewHolder.itemTitle?.text = novel.getTitle()
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context,FrontEndEditNovelActivity::class.java)
            intent.putExtra("novelId",novel.getNovelId())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return novelList.size
    }
}