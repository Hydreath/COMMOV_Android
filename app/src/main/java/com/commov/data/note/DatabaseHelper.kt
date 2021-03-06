package com.commov.data.note

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHelper(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
){
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TABLE_NOTES($COLLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLLUMN_CREATED_AT INTEGER NOT NULL, $COLLUMN_RELEVANT_AT INTEGER NOT NULL, $COLLUMN_TITLE TEXT, $COLLUMN_DESCRIPTION TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        this.onCreate(db)
    }

    fun addNote(note: Note?){
        if(note == null)
            return

        val values = ContentValues()
        values.put(COLLUMN_CREATED_AT, note.createdAt.time)
        values.put(COLLUMN_RELEVANT_AT, note.relevantAt.time)
        values.put(COLLUMN_DESCRIPTION, note.description)
        values.put(COLLUMN_TITLE, note.title)
        this.writableDatabase.insert(TABLE_NOTES, null, values);
        this.getAllNotes()
        this.writableDatabase.close()
    }

    fun getAllNotes(): ArrayList<Note>{
        val cursor: Cursor = this.writableDatabase.rawQuery("Select * from $TABLE_NOTES", null)
        val results = java.util.ArrayList<Note>()
        while (cursor.moveToNext()) {
            println(cursor.getLong(1))
            println(cursor.getLong(2))
            results.add(
                Note(
                    cursor.getInt(0),
                    cursor.getString(3),
                    cursor.getString(4),
                    Date(cursor.getLong(1)),
                    Date(cursor.getLong(2))
                )
            )
        }
        cursor.close()
        this.writableDatabase.close()
        return results
    }

    fun updateNote(note: Note): Boolean{
        val cv: ContentValues = ContentValues()
        cv.put(COLLUMN_CREATED_AT, note.createdAt.time)
        cv.put(COLLUMN_RELEVANT_AT, note.relevantAt.time)
        cv.put(COLLUMN_DESCRIPTION, note.description)
        cv.put(COLLUMN_TITLE, note.title)
        val ret = writableDatabase.update(TABLE_NOTES, cv, "$COLLUMN_ID = ${note.id}", null) > 0
        writableDatabase.close()
        return ret


    }

    fun deleteNote(note: Note): Boolean {
        return this.writableDatabase.delete(TABLE_NOTES, "$COLLUMN_ID =  ${note.id}", null) > 0
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "notes_db"
        val TABLE_NOTES = "notes"
        val COLLUMN_ID = "note_id"
        val COLLUMN_DESCRIPTION = "description"
        val COLLUMN_CREATED_AT = "created_at"
        val COLLUMN_RELEVANT_AT = "relevant_at"
        val COLLUMN_TITLE = "title"
    }
}