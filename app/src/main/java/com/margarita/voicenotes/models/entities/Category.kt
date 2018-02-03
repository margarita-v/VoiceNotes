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

    /**
     * Function which checks if the category's list of notes contain the given note
     * @param noteItem Note item which will be searched in the category's list of notes
     * @return True if the list of notes contain the given note, false otherwise
     */
    fun containNote(noteItem: NoteItem): Boolean = findNote(noteItem.id) != null

    /**
     * Function for removing a note from the category's note list
     * @param noteItem Note item which will be removed
     */
    fun removeNote(noteItem: NoteItem) {
        notes.remove(findNote(noteItem.id))
    }

    /**
     * Function for searching a note item in the category's list of notes
     * @param id ID of searched note item
     * @return Searched note item or null if the note item was not found
     */
    private fun findNote(id: Long): NoteItem? = notes.find { it.id == id }

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