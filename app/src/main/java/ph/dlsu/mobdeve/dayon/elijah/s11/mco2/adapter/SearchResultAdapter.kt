package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.EpisodeViewerActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ItemSearchresultBinding

class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private var searchResultList = ArrayList<String>()
    private lateinit var context: Context

    constructor(context: Context, searchResultList: ArrayList<String>){
        this.context = context
        this.searchResultList = searchResultList
    }


    override fun getItemCount(): Int {
        return searchResultList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultAdapter.SearchResultViewHolder {
        val searchResult = ItemSearchresultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(searchResult)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bindName(searchResultList[position])
    }

    inner class SearchResultViewHolder(private val searchResult: ItemSearchresultBinding) : RecyclerView.ViewHolder(searchResult.root), View.OnClickListener {
        var name = ""

        init{
            itemView.setOnClickListener {
                val intent = Intent(context, EpisodeViewerActivity::class.java)
                context.startActivity(intent)
            }
        }

        fun bindName(name:String){
            this.name = name
            searchResult.searchResultListTV.text = "$name"
        }

        override fun onClick(v: View?){
            Toast.makeText(context, "${name}", Toast.LENGTH_SHORT).show()
        }
    }




}