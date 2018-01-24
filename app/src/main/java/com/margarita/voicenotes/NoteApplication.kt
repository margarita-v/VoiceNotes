package com.margarita.voicenotes

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class NoteApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Realm initialization
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}