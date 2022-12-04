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
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityRemoveFollowersBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ItemRemoveFollowersBinding


class RemoveFollowersAdapter : RecyclerView.Adapter<RemoveFollowersAdapter.RemoveFollowersViewHolder>{
    private var followerArrayList = ArrayList<String>()
    private lateinit var context: Context
    private lateinit var mode: String

    constructor(context: Context, followerArrayList: ArrayList<String>, mode: String){
        this.context = context
        this.followerArrayList = followerArrayList
        this.mode = mode
    }

    override fun getItemCount(): Int {
        return followerArrayList.size
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RemoveFollowersAdapter.RemoveFollowersViewHolder {
        val removeFollowerBtnBinding = ItemRemoveFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RemoveFollowersViewHolder(removeFollowerBtnBinding)
    }

    override fun onBindViewHolder(holder: RemoveFollowersViewHolder, position: Int) {
        holder.bindName(followerArrayList[position])
    }

    inner class RemoveFollowersViewHolder(private val removeFollowersBtnBinding: ItemRemoveFollowersBinding) : RecyclerView.ViewHolder(removeFollowersBtnBinding.root), View.OnClickListener {
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
            removeFollowersBtnBinding.removeFollowerNameTV.text = "$name"
        }

        override fun onClick(v: View?){
            Toast.makeText(context, "${name}", Toast.LENGTH_SHORT).show()
        }
    }







}