package com.margarita.voicenotes.models.entities

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Category(@PrimaryKey var id: Long = 0,
                    var name: String = "",
                    var notes: RealmList<NoteItem>? = null): RealmObject()