package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model

class Tag {
    private var novelId:String=""
    private var tagName:String=""
    constructor()

    constructor(novelId: String, tagName: String) {
        this.novelId = novelId
        this.tagName = tagName
    }
    fun getNovelId():String{
        return novelId
    }
    fun getTagName():String{
        return tagName
    }
    fun setNovelId(novelId: String)
    {
        this.novelId=novelId
    }

    fun setReview(tagName: String)
    {
        this.tagName=tagName
    }}