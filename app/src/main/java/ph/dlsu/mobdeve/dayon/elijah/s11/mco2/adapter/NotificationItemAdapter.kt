package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.FrontEndNovelActivity


class NotificationItemAdapter : RecyclerView.Adapter<NotificationItemAdapter.ViewHolder>(){
    private var notificationArray = arrayOf(
        "You got a comment by @shimara",
        "@garian reported a grammar error",
        "Reincarnated slave reached 1000 stars!"
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notificationText: TextView


        init {
            notificationText = itemView.findViewById(R.id.notification_text)

            itemView.setOnClickListener {
                var position: Int = getAbsoluteAdapterPosition()
                val context = itemView.context
                val intent = Intent(context, FrontEndNovelActivity::class.java).apply {//
                    putExtra("NUMBER", position)
                    putExtra("CODE", notificationText.text)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_notification, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.notificationText.text = notificationArray[i]
    }

    override fun getItemCount(): Int {
        return notificationArray.size
    }


    /*private var notificationArrayList = ArrayList<String>()
    private lateinit var context: Context

    constructor(context: Context, notificationArrayList: ArrayList<String>){
        this.context = context
        this.notificationArrayList = notificationArrayList
    }

    override fun getItemCount(): Int {
        return notificationArrayList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationItemAdapter.NotificationItemViewHolder {
        val itemNotificationBinding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationItemViewHolder(itemNotificationBinding)
    }

    override fun onBindViewHolder(holder: NotificationItemViewHolder, position: Int) {
        holder.bindName(notificationArrayList[position])
    }

    inner class NotificationItemViewHolder(private val itemNotificationBinding: ItemNotificationBinding) : RecyclerView.ViewHolder(itemNotificationBinding.root), View.OnClickListener {
        var name = ""

        init{
            itemView.setOnClickListener(this)
        }

        fun bindName(name:String){
            this.name = name
            itemNotificationBinding.notificationText.text = "$name"
        }

        override fun onClick(v: View?){
            Toast.makeText(context, "${name}", Toast.LENGTH_SHORT).show()
        }
    }*/

}