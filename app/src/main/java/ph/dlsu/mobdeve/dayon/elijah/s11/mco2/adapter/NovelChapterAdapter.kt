package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.NovelChapterBtnBinding

class NovelChapterAdapter : RecyclerView.Adapter<NovelChapterAdapter.NovelChapterViewHolder> {
    private var chapterTitleArrayList = ArrayList<String>()
    private lateinit var context: Context

    constructor(context: Context, chapterTitleArrayList: ArrayList<String>){
        this.context = context
        this.chapterTitleArrayList = chapterTitleArrayList
    }


    override fun getItemCount(): Int {
        return chapterTitleArrayList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NovelChapterAdapter.NovelChapterViewHolder {
        val novelChapterBtnBinding = NovelChapterBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NovelChapterViewHolder(novelChapterBtnBinding)
    }

    override fun onBindViewHolder(holder: NovelChapterViewHolder, position: Int) {
        holder.bindName(chapterTitleArrayList[position])
    }

    inner class NovelChapterViewHolder(private val novelChapterBtnBinding: NovelChapterBtnBinding) : RecyclerView.ViewHolder(novelChapterBtnBinding.root), View.OnClickListener {
        var name = ""

        init{
            itemView.setOnClickListener(this)
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

