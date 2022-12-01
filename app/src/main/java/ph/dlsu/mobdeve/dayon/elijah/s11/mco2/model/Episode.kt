package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model

import android.icu.text.RelativeDateTimeFormatter

class Episode {
    private var episodeId: String = ""
    private var novelTitle:String = ""
    private var novelId: String = ""
    private var uid: String = ""
    private var episodeTitle: String = ""
    private var content: String = ""
    private var releaseDateTime: String = ""
    private var isDraft: Boolean = true
    private var isPublished: Boolean = false
    constructor()

    constructor(episodeId: String, novelTitle:String,
                novelId: String, uid:String,
                episodeTitle:String, content: String,
                releaseDate:String, isDraft:Boolean,isPublished:Boolean) {
        this.episodeId = episodeId
        this.novelTitle = novelTitle
        this.novelId = novelId
        this.uid = uid
        this.episodeTitle = episodeTitle
        this.content = content
        this.releaseDateTime = releaseDate
        this.isDraft = isDraft
        this.isPublished = isPublished
    }
    fun getEpisodeId():String{
        return episodeId
    }
    fun getNovelId():String{
        return novelId
    }
    fun getUid():String{
        return uid
    }
    fun getEpisodeTitle():String{
        return episodeTitle
    }
    fun getNovelTitle():String{
        return novelTitle
    }
    fun getContent():String{
        return content
    }
    fun getReleaseDateTime():String{
        return releaseDateTime
    }
    fun getIsDraft():Boolean{
        return isDraft
    }
    fun getPublished():Boolean{
        return isPublished
    }

    fun setEpisodeId(episodeId:String){
        this.episodeId=episodeId
    }
    fun setNovelId(novelId:String){
        this.novelId = novelId
    }
    fun setUid(uid: String){
        this.uid = uid
    }
    fun setNovelTitle(novelTitle:String){
        this.novelTitle=novelTitle
    }
    fun setEpTitle(episodeTitle:String){
        this.episodeTitle=episodeTitle
    }
    fun setContent(content:String){
        this.content=content
    }
    fun setReleaseDateTime(releaseDateTime:String){
        this.releaseDateTime=releaseDateTime
    }
    fun setIsDraft(isDraft: Boolean){
        this.isDraft=isDraft
    }
    fun setIsPublished(isPublished: Boolean){
        this.isPublished = isPublished
    }
}