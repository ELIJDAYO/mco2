package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.Interface

import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Tag

interface FirebaseCallback {
    fun onCallBack1(list: ArrayList<String>)
    fun onCallBack2(list: ArrayList<String>)
    fun onCallBack3(list: ArrayList<Novel>)
}