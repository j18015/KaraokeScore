package com.example.karaokescore

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Score : RealmObject() {
    @PrimaryKey
    var id : Long = 0
    var date : Date = Date()
    var title : String = ""
    var singer : String = ""
    var Score : String = ""
}