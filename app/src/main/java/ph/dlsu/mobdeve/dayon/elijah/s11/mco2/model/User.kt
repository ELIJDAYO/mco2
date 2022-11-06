package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

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
}