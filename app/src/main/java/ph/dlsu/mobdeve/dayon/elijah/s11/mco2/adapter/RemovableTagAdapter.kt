package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndNovelActivity


class RemovableTagAdapter : RecyclerView.Adapter<RemovableTagAdapter.ViewHolder>() {
    private val tags = arrayOf(
        "Dark Fantasy",
        "Sci-fi",
        "Cute",)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTag: TextView? = null

        init {
            itemTag = itemView.findViewById(R.id.tv_title_iet)

        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_remove_tag, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTag?.text = tags[i]
    }

    override fun getItemCount(): Int {
        return tags.size
    }
}