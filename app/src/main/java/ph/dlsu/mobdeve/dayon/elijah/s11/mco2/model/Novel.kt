package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model

class Novel {
    private var novelId: String = ""
    private var title: String = ""
    private var synopsis: String = ""
    private var imageUri: String = ""
    private var numEpisode: String = ""
    constructor()

    constructor(novelId: String, title: String, synopsis: String, imageUri: String) {
        this.novelId = novelId
        this.title = title
        this.synopsis = synopsis
        this.imageUri = imageUri
    }
    fun getNovelId():String{
        return novelId
    }
    fun getTitle():String{
        return title
    }
    fun getSynopsis():String{
        return synopsis
    }
    fun getImageUri():String{
        return imageUri
    }
    fun setPostId(postId:String){
        this.novelId=novelId
    }
    fun setTitle(title:String){
        this.title=title
    }
    fun setSynopsis(synopsis:String){
        this.synopsis=synopsis
    }
    fun setImageUri(imageUri:String){
        this.imageUri=imageUri
    }
}