package com.margarita.voicenotes.models.entities

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.margarita.voicenotes.common.getCategory
import com.margarita.voicenotes.common.parseDate
import com.margarita.voicenotes.common.parseStringToUri
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey

open class NoteItem(@PrimaryKey var id: Long = 0,
                    var description: String = "",
                    private var date: Long = 0,
                    var photoUri: String? = null,
                    var croppedPhotoUri: String? = null,
                    @LinkingObjects("notes")
                    val categories: RealmResults<Category>? = null)
    : RealmObject(), Parcelable {

    /**
     * Function for parsing a date to the nice format
     */
    fun parseDate(): String = date.parseDate()

    /**
     * Function which checks if the note's fields are different to given fields
     */
    fun isDifferentFrom(description: String,
                      categoryId: Long?,
                      photoUri: String? = null,
                      croppedPhotoUri: String? = null): Boolean
            = this.description != description ||
                this.photoUri != photoUri ||
                this.croppedPhotoUri != croppedPhotoUri ||
                getCategory(this)?.id != categoryId

    /**
     * Function for getting a photoUri parsed from String to Uri
     */
    fun getPhotoUri(): Uri? = photoUri?.parseStringToUri()

    /**
     * Function for getting a cropped photo uri parsed from String to Uri
     */
    fun getCroppedPhotoUri(): Uri? = croppedPhotoUri?.parseStringToUri()

    //region Parcelable implementation
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(description)
        parcel.writeLong(date)
        parcel.writeString(photoUri)
        parcel.writeString(croppedPhotoUri)
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