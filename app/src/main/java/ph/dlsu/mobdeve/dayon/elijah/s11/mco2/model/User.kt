package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model
class User {
    private var uid: String = ""
    private var username: String = ""
    private var birthday: String = ""
    private var gender: String = ""
    private var image: String = ""

    constructor()

    constructor(username: String, birthday: String, bio: String, uid: String, image: String) {
        this.username = username
        this.uid = uid
        this.birthday = birthday
        this.gender = bio
        this.image = image
    }
    //getters and Setters
    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getUid(): String {
        return uid
    }

    fun setUid(uid: String) {
        this.uid = uid
    }

    fun getBirthday(): String {
        return birthday
    }

    fun setBirthday(birthday: String) {
        this.birthday = birthday
    }

    fun getGender(): String {
        return gender
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun getImage(): String {
        return image
    }

    fun setImage(image: String) {
        this.image = image
    }
}