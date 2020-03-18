package com.commov.data.note

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Note() : Parcelable{
    var id: Int? = null
    var title: String = ""
    var description: String = ""
    var createdAt: Date = Date()
    lateinit var relevantAt: Date

    constructor(parcel: Parcel) : this() {
        this.id = parcel.readInt()
        this.title = parcel.readString().toString()
        this.description = parcel.readString().toString()
        this.createdAt = Date(parcel.readLong())
        this.relevantAt = Date(parcel.readLong())
    }

    constructor(title: String, description: String, date: Date) : this() {
        this.title = title
        this.description = description
        this.relevantAt = date
    }

    constructor(id: Int, title: String, description: String, createdAt: Date, releventAt: Date): this() {
        this.id = id
        this.title = title
        this.description = description
        this.relevantAt = releventAt
        this.createdAt = createdAt
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(this.id!!)
        dest?.writeString(this.title)
        dest?.writeString(this.description)
        dest?.writeLong(this.createdAt.time)
        dest?.writeLong(this.relevantAt.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }


}