package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model

class Episode {
    private var episodeId: String = ""
    private var novelId: String = ""
    private var title: String = ""
    private var content: String = ""
    private var releaseDate: String = ""
    private var isDraft: Boolean = true
    constructor()

    constructor(episodeId: String, novelId: String, title: String, content: String, releaseDate:String, isDraft:Boolean) {
        this.episodeId = episodeId
        this.novelId = novelId
        this.title = title
        this.content = content
        this.releaseDate = releaseDate
        this.isDraft = isDraft
    }
    fun getEpisodeId():String{
        return episodeId
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
    fun setEpisodeId(episodeId:String){
        this.episodeId=episodeId
    }
    fun setTitle(title:String){
        this.title=title
    }
    fun setContent(content:String){
        this.content=content
    }
    fun setIsDraft(isDraft: Boolean){
        this.isDraft=isDraft
    }
}