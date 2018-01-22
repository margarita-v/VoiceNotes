package com.margarita.voicenotes.models

import android.os.Parcel
import android.os.Parcelable
import com.margarita.voicenotes.common.parseDate

data class NoteItem(val id: Int,
                    var description: String,
                    var date: Long,
                    var photoPath: String? = null): Parcelable {

    fun parseDate(): String = date.parseDate()

    //region Parcelable implementation
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeLong(date)
        parcel.writeString(photoPath)
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