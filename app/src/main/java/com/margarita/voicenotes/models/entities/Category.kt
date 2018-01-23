package com.margarita.voicenotes.models.entities

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class Category(@PrimaryKey val id: Int,
                    var name: String,
                    private var notes: RealmList<NoteItem>): RealmObject()