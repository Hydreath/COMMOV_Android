package com.commov.Note.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Parcel
import android.os.Parcelable

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME , null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TABLE_NOTES($COLLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLLUMN_START_DATE INTEGER, $COLLUMN_END_DATE INTEGER, $COLLUMN_WEEK_DAY INTEGER, $COLLUMN_DESCRIPTION TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS NOTES")
        this.onCreate(db)
    }

    fun addNote(note: Note){
        val values = ContentValues()
        values.put(COLLUMN_START_DATE, note.startDate?.time)
        values.put(COLLUMN_END_DATE, note.endDate?.time)
        values.put(COLLUMN_DESCRIPTION, note.description)
        values.put(COLLUMN_WEEK_DAY, note.weekDays)
        this.writableDatabase.insert(TABLE_NOTES, null, values);
        this.getAllNotes()
        this.writableDatabase.close()
        println("Added new note to db")
    }

    fun getAllNotes(): ArrayList<Note>{
        println("----------------GETTING NOTES--------------------")
        val curor: Cursor = this.writableDatabase.rawQuery("Select * from $TABLE_NOTES", null)
        val results = java.util.ArrayList<Note>()
        while (curor.moveToNext()) {
            println(curor.getString(4))
            results.add(Note(curor.getString(4), curor.getInt(1), curor.getInt(2), curor.getInt(3)))
        }
        curor.close()
        this.writableDatabase.close()
        return results
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "notes_db"
        val TABLE_NOTES = "notes"
        val COLLUMN_ID = "note_id"
        val COLLUMN_START_DATE = "start_date"
        val COLLUMN_END_DATE = "end_date"
        val COLLUMN_WEEK_DAY = "week_day"
        val COLLUMN_DESCRIPTION = "description"
    }
}