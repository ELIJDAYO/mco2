package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.User


class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private var userList= arrayListOf<User>()
    private var context:Context

    constructor(
        context:Context,
        userList: ArrayList<User>
    ){
        this.context = context
        this.userList = userList
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var circleImageView: CircleImageView? = null
        var genderTv: TextView? = null
        var usernameTv: TextView? = null
        init {
            circleImageView = itemView.findViewById(R.id.civ_user_iu)
            usernameTv = itemView.findViewById(R.id.tv_username_iu)
            genderTv = itemView.findViewById(R.id.tv_gender_iu)

        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_user, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val user = userList[i]
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile_user).into(viewHolder.circleImageView) //add picasso dependency for image caching and downloading
        viewHolder.usernameTv?.text = user.getUsername()
        viewHolder.genderTv?.text = user.getGender()
    }
    override fun getItemCount(): Int {
        return userList.size
    }
}