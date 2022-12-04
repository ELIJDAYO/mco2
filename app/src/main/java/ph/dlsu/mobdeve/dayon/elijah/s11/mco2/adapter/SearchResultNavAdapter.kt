package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndNovelActivity

class SearchResultNavAdapter : RecyclerView.Adapter<SearchResultNavAdapter.ViewHolder>(){
    private var searchResultNavArray = arrayOf(
        "Goblin Slayer",
        "Random Title",
        "Title1203193012"
    )

    private var searchResultNavStarArray = arrayOf(
        12,
        100,
        1
    )


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var searchResultText: TextView
        var searchResultStar: TextView


        init {
            searchResultText = itemView.findViewById(R.id.tv_title_inp)
            searchResultStar = itemView.findViewById(R.id.tv_count_inp)

            itemView.setOnClickListener {
                var position: Int = getAbsoluteAdapterPosition()
                val context = itemView.context
                val intent = Intent(context, FrontEndNovelActivity::class.java).apply {//
                    putExtra("NUMBER", position)
                    putExtra("CODE", searchResultText.text)
                }
                context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_searchresult_nav, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.searchResultText.text = searchResultNavArray[i]
        viewHolder.searchResultStar.text = searchResultNavStarArray[i].toString()
    }

    override fun getItemCount(): Int {
        return searchResultNavArray.size
    }



}