package com.margarita.voicenotes.models.entities

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Category(@PrimaryKey var id: Long = 0,
                    var name: String = "",
                    var notes: RealmList<NoteItem> = RealmList())
    : RealmObject(), Parcelable {

    override fun toString(): String = name

    //region Parcelable implementation
    constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readString()) {
        parcel.readTypedList(notes, NoteItem.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeTypedList(notes)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
    //endregion
}