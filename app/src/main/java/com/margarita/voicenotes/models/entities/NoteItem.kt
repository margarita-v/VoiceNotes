package com.margarita.voicenotes.models.entities

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.margarita.voicenotes.common.parseDate
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey

data class NoteItem(@PrimaryKey val id: Int,
                    var description: String,
                    var date: Long,
                    var photoUri: Uri? = null,
                    var croppedPhotoUri: Uri? = null,
                    @LinkingObjects("notes")
                    private val categories: RealmResults<Category>? = null)
    : RealmObject(), Parcelable {

    fun parseDate(): String = date.parseDate()

    //region Parcelable implementation
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readLong(),
            Uri.parse(parcel.readString()),
            Uri.parse(parcel.readString()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeLong(date)
        parcel.writeString(photoUri.toString())
        parcel.writeString(croppedPhotoUri.toString())
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<NoteItem> {
        override fun createFromParcel(parcel: Parcel): NoteItem {
            return NoteItem(parcel)
        }

        override fun newArray(size: Int): Array<NoteItem?> {
            return arrayOfNulls(size)
        }
    }
    //endregion
}