package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.coroutines.delay
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.CalendarAndTimeActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditExistingEpisodeActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EpisodeViewerActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.NovelChapterBtnBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.User

class NovelEpisodeAdapter : RecyclerView.Adapter<NovelEpisodeAdapter.ViewHolder> {
    private var episodeList = ArrayList<Episode>()
    private lateinit var context: Context
    private var mode:String="view"

    constructor(context: Context, episodeList: ArrayList<Episode>, mode: String){
        this.context = context
        this.episodeList = episodeList
    }


    override fun getItemCount(): Int {
        return episodeList.size
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_work_repo, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val episode = episodeList[i]
        viewHolder.itemEpisodeTitle?.text = episode.getEpisodeTitle()
        viewHolder.itemNovelTitle?.text = episode.getNovelTitle()
        viewHolder.itemReleaseDate?.text = episode.getReleaseDateTime()

        viewHolder.itemView.setOnClickListener {
            if (mode=="view"){
                val intent = Intent(context, EpisodeViewerActivity::class.java)
                intent.putExtra("episodeId",episode.getEpisodeId())
                context.startActivity(intent)
            }
            else if(mode == "edit"){
                val intent = Intent(context, EditExistingEpisodeActivity::class.java)
                intent.putExtra("episodeId",episode.getEpisodeId())
                context.startActivity(intent)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemNovelTitle: TextView? = null
        var itemEpisodeTitle: TextView? = null
        var itemReleaseDate: TextView? = null

        init{
            itemEpisodeTitle = itemView.findViewById(R.id.tv_ep_title_iwp)
            itemNovelTitle = itemView.findViewById(R.id.tv_novel_title_iwp)
            itemReleaseDate = itemView.findViewById(R.id.tv_release_date_iwp)
        }
    }
}

