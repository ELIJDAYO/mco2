package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model

class Novel {
    private var novelId: String = ""
    private var uid: String = ""
    private var title: String = ""
    private var synopsis: String = ""
    private var imageUri: String = ""
    private var numEpisodes: Int = 0

    constructor()

    constructor(novelId: String,
                uid:String,
                title: String,
                synopsis: String,
                imageUri: String,
                numEpisodes: Int,
                ) {
        this.novelId = novelId
        this.uid = uid
        this.title = title
        this.synopsis = synopsis
        this.imageUri = imageUri
        this.numEpisodes = numEpisodes
    }
    fun getNovelId():String{
        return novelId
    }
    fun getTitle():String{
        return title
    }
    fun getUid():String{
        return uid
    }
    fun getSynopsis():String{
        return synopsis
    }
    fun getImageUri():String{
        return imageUri
    }
    fun getNumEp():Int{
        return numEpisodes
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
    fun setUid(uid:String){
        this.uid = uid
    }
    fun setNumEp(numEpisodes:Int){
        this.numEpisodes = numEpisodes
    }
}