package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.CalendarAndTimeActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel


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
    private var repoList:ArrayList<Episode>
    private lateinit var context:Context

    constructor(context: Context,repoList: ArrayList<Episode>){
        this.context=context
        this.repoList = repoList
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemNovelTitle: TextView? = null
        var itemEpisodeTitle: TextView? = null
        var itemReleaseDate: TextView? = null

        init {
            itemEpisodeTitle = itemView.findViewById(R.id.tv_ep_title_iwp)
            itemNovelTitle = itemView.findViewById(R.id.tv_novel_title_iwp)
            itemReleaseDate = itemView.findViewById(R.id.tv_release_date_iwp)

        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_work_repo, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val repo = repoList[i]
        viewHolder.itemEpisodeTitle?.text = repo.getEpisodeTitle()
        viewHolder.itemNovelTitle?.text = repo.getNovelTitle()
        val releasedDatetime = repo.getReleaseDateTime()
        if(releasedDatetime.isNullOrBlank()){
            viewHolder.itemReleaseDate?.text = "Draft"
        }else{
            viewHolder.itemReleaseDate?.text = releasedDatetime
        }

        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context,CalendarAndTimeActivity::class.java)
            intent.putExtra("episodeId",repo.getEpisodeId())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

}