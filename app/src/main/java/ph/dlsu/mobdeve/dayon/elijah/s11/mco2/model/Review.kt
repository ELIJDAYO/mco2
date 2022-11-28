package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model

class Review {
    private var publisher:String=""
    private var review:String=""

    constructor()

    constructor(publisher: String, review: String) {
        this.publisher = publisher
        this.review = review
    }

    fun getPublisher():String{
        return publisher
    }
    fun getReview():String{
        return review
    }

    fun setPublisher(publisher: String)
    {
        this.publisher=publisher
    }

    fun setReview(review: String)
    {
        this.review=review
    }
}