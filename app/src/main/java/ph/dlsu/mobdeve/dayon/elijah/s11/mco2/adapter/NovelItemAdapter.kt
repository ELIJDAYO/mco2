package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditFrontEndNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndNovelActivity


class NovelItemAdapter : RecyclerView.Adapter<NovelItemAdapter.ViewHolder>() {
    private val titleNovel = arrayOf(
        "Goblin Slayer",
        "灰まみれの騎士",
        "13番目の転生者",
        "High School of the Elites")
    private val numEpisodes = arrayOf(
        "21", "33", "19", "100",
    )
    private val date = arrayOf(
        "Jan 1", "Feb 1", "March 1", "April 1")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView? = null
        var itemDate: TextView? = null
        var itemCount: TextView? = null
        init {
            itemTitle = itemView.findViewById(R.id.tv_title_inp)
            itemDate = itemView.findViewById(R.id.tv_date_inp)
            itemCount = itemView.findViewById(R.id.tv_count_inp)
            itemView.setOnClickListener {
                var position: Int = bindingAdapterPosition
                val context = itemView.context
//                val intent = Intent(context, HomeFragment::class.java).apply {
//                    putExtra("NUMBER", position)
//                    putExtra("title", itemTitle?.text)
//                    putExtra("date", itemDate?.text)
//                    putExtra("num_episodes", itemCount?.text)
                    if (position >= 0){
                        itemView.setOnClickListener{
                            val intent = Intent(context, FrontEndNovelActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
//                context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_novel_post, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle?.text = titleNovel[i]
        viewHolder.itemDate?.text = date[i]
        viewHolder.itemCount?.text = numEpisodes[i]
    }

    override fun getItemCount(): Int {
        return titleNovel.size
    }
}