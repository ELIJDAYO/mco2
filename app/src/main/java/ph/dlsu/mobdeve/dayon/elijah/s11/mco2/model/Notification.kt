package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model

import android.icu.text.RelativeDateTimeFormatter

class Notification {
    private var ownerId: String = ""
    private var content:String = ""
    private var receiverId: String = ""

    constructor()

    constructor(ownerId: String, content:String,
                receiverId: String) {

        this.ownerId = ownerId
        this.content = content
        this.receiverId = receiverId

    }

    fun getOwnerId(): String{
        return ownerId
    }

    fun getContent(): String{
        return content
    }

    fun getReceiverId(): String{
        return receiverId
    }

    fun setOwnerId(ownerId: String){
        this.ownerId = ownerId
    }

    fun setContent(content: String){
        this.content = content
    }

    fun setReceiverId(receiverId: String){
        this.receiverId = receiverId
    }
}