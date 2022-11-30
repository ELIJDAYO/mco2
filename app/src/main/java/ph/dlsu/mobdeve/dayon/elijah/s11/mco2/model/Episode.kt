package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model

class Episode {
    private var episodeId: String = ""
    private var novelId: String = ""
    private var uid: String = ""
    private var title: String = ""
    private var content: String = ""
    private var releaseDate: String = ""
    private var releaseTime: String = ""
    private var isDraft: Boolean = true
    constructor()

    constructor(episodeId: String, novelId: String, uid:String, title: String, content: String, releaseDate:String, isDraft:Boolean) {
        this.episodeId = episodeId
        this.novelId = novelId
        this.uid = uid
        this.title = title
        this.content = content
        this.releaseDate = releaseDate
        this.releaseTime = releaseTime
        this.isDraft = isDraft
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
    fun getTitle():String{
        return title
    }
    fun getContent():String{
        return content
    }
    fun getReleaseDate():String{
        return releaseDate
    }
    fun getIsDraft():Boolean{
        return isDraft
    }
    fun getReleaseTime():String{
        return releaseTime
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
    fun setTitle(title:String){
        this.title=title
    }
    fun setContent(content:String){
        this.content=content
    }
    fun setReleaseDate(content:String){
        this.releaseDate=releaseDate
    }
    fun setReleaseTime(content:String){
        this.releaseTime=releaseTime
    }
    fun setIsDraft(isDraft: Boolean){
        this.isDraft=isDraft
    }
}