package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.CalendarAndTimeActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode


class WorkRepoItemAdapter : RecyclerView.Adapter<WorkRepoItemAdapter.ViewHolder> {
    private val titleNovel = arrayOf(
        "Battle Ship Yamato Awakens",
        "Dungeon Seeker",
        "Dungeon Seeker")
    private val titleEpisode = arrayOf(
        "Episode 10",
        "Miracle BOy",
        "Despair falls upon all")
    private val releaseDate = arrayOf(
        "2022-11-30",
        "2022-11-26",
        "Draft")
    private var context: Context
    private var novelTitleList: ArrayList<String>
    private var repoList:ArrayList<Episode>


    constructor(context: Context,novelTitleList: ArrayList<String>,repoList: ArrayList<Episode>){
        this.context = context
        this.novelTitleList = novelTitleList
        this.repoList = repoList
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemNovelTitle: TextView? = null
        var itemEpisodeTitle: TextView? = null
        var itemReleaseDate: TextView? = null

        init {
            itemNovelTitle = itemView.findViewById(R.id.tv_ep_title_iwp)
            itemEpisodeTitle = itemView.findViewById(R.id.tv_novel_title_iwp)
            itemReleaseDate = itemView.findViewById(R.id.tv_release_date_iwp)

            itemView.setOnClickListener {
                var position: Int = bindingAdapterPosition
                val context = itemView.context
//                val intent = Intent(context, HomeFragment::class.java).apply {
//                    putExtra("NUMBER", position)
                    if (position >= 0){
                        itemView.setOnClickListener{
                            val intent = Intent(context, CalendarAndTimeActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
//                context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_work_repo, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val repo = repoList[i]
        val novelTitle = novelTitleList[i]
        viewHolder.itemNovelTitle?.text = novelTitle
        viewHolder.itemEpisodeTitle?.text = repo.getTitle()
        viewHolder.itemReleaseDate?.text = repo.getReleaseDate()
    }

    override fun getItemCount(): Int {
        return repoList.size
    }
}