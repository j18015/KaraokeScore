package com.example.karaokescore

import android.app.Application
import io.realm.Realm

class KaraokeScoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}