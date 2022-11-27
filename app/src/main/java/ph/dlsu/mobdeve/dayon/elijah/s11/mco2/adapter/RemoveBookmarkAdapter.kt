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
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ItemRemoveBookmarkBinding

class RemoveBookmarkAdapter : RecyclerView.Adapter<RemoveBookmarkAdapter.RemoveBookmarkViewHolder>{
    private var bookmarkArrayList = ArrayList<String>()
    private lateinit var context: Context
    private lateinit var mode: String

    constructor(context: Context, bookmarkArrayList: ArrayList<String>, mode: String){
        this.context = context
        this.bookmarkArrayList = bookmarkArrayList
        this.mode = mode
    }

    override fun getItemCount(): Int {
        return bookmarkArrayList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RemoveBookmarkAdapter.RemoveBookmarkViewHolder {
        val removeBookmarkBtnBinding = ItemRemoveBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RemoveBookmarkViewHolder(removeBookmarkBtnBinding)
    }

    override fun onBindViewHolder(holder: RemoveBookmarkViewHolder, position: Int) {
        holder.bindName(bookmarkArrayList[position])
    }

    inner class RemoveBookmarkViewHolder(private val removeBookmarkBtnBinding: ItemRemoveBookmarkBinding) : RecyclerView.ViewHolder(removeBookmarkBtnBinding.root), View.OnClickListener {
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
            removeBookmarkBtnBinding.removeBookmarkText.text = "$name"
        }

        override fun onClick(v: View?){
            Toast.makeText(context, "${name}", Toast.LENGTH_SHORT).show()
        }
    }




}