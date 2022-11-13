package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter;

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndNovel

class SearchHistoryAdapter : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>(){
    private var searchHistoryArray = arrayOf(
        "Goblin Slayer",
        "Random Title",
        "Title1203193012"
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var searchHistoryText: TextView


        init {
            searchHistoryText = itemView.findViewById(R.id.searchHistoryText)

            itemView.setOnClickListener {
                var position: Int = getAbsoluteAdapterPosition()
                val context = itemView.context
                val intent = Intent(context, FrontEndNovel::class.java).apply {//
                    putExtra("NUMBER", position)
                    putExtra("CODE", searchHistoryText.text)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_searchhistory, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.searchHistoryText.text = searchHistoryArray[i]
    }

    override fun getItemCount(): Int {
        return searchHistoryArray.size
    }




}
