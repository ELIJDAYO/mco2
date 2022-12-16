package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel


class NovelItemAdapter : RecyclerView.Adapter<NovelItemAdapter.ViewHolder>{
    private var novelList:ArrayList<Novel>
    private var novelDateUpdatedList:ArrayList<String>
    private var context:Context
    constructor(context: Context,novelList:ArrayList<Novel>, novelDateUpdatedList:ArrayList<String>){
        this.context = context
        this.novelList = novelList
        this.novelDateUpdatedList = novelDateUpdatedList
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView? = null
        var itemDate: TextView? = null
        var itemCount: TextView? = null
        var itemNumEp: TextView?= null
        init {
            itemTitle = itemView.findViewById(R.id.tv_title_inp)
            itemDate = itemView.findViewById(R.id.tv_date_inp)
            itemCount = itemView.findViewById(R.id.tv_count_inp)
            itemNumEp = itemView.findViewById(R.id.tv_sum_of_episodes_inp)

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_novel_post, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val novel = novelList[position]
        val releasedDate = novelDateUpdatedList[position]
        holder.itemTitle?.text = novel.getTitle()
        holder.itemDate?.text = releasedDate
        holder.itemCount?.text = "updating"
        holder.itemNumEp?.text = novel.getNumEpisodes()

        holder.itemView.setOnClickListener {
            val intent = Intent(context,FrontEndNovelActivity::class.java)
            intent.putExtra("novelId",novel.getNovelId())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return novelList.size
    }
}
