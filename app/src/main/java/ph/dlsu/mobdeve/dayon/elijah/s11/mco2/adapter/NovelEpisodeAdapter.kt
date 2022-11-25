package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EditNovelActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EpisodeViewerActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.NovelChapterBtnBinding

class NovelEpisodeAdapter : RecyclerView.Adapter<NovelEpisodeAdapter.NovelChapterViewHolder> {
    private var chapterTitleArrayList = ArrayList<String>()
    private lateinit var context: Context
    private lateinit var mode: String

    constructor(context: Context, chapterTitleArrayList: ArrayList<String>, mode: String){
        this.context = context
        this.chapterTitleArrayList = chapterTitleArrayList
        this.mode = mode
    }


    override fun getItemCount(): Int {
        return chapterTitleArrayList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NovelEpisodeAdapter.NovelChapterViewHolder {
        val novelChapterBtnBinding = NovelChapterBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NovelChapterViewHolder(novelChapterBtnBinding)
    }

    override fun onBindViewHolder(holder: NovelChapterViewHolder, position: Int) {
        holder.bindName(chapterTitleArrayList[position])
    }

    inner class NovelChapterViewHolder(private val novelChapterBtnBinding: NovelChapterBtnBinding) : RecyclerView.ViewHolder(novelChapterBtnBinding.root), View.OnClickListener {
        var name = ""

        init{
            itemView.setOnClickListener {
                if (mode=="view"){
                    val intent = Intent(context, EpisodeViewerActivity::class.java)
                    context.startActivity(intent)
                }
                else if(mode == "edit"){
                    val intent = Intent(context, EditNovelActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }

        fun bindName(name:String){
            this.name = name
            novelChapterBtnBinding.novelChapterLayout.text = "$name"
        }

        override fun onClick(v: View?){
            Toast.makeText(context, "${name}", Toast.LENGTH_SHORT).show()
        }
    }

}

