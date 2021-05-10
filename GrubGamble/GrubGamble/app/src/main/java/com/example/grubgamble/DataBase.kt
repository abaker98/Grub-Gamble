package com.example.grubgamble

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBase(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(query)
        Log.d("DataBase", "onCreate")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(query)
        Log.d("DataBase", "onUpgrade")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("DataBase", "onDowngrade")
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "winner.db"
        val query = "CREATE TABLE " +
                List.table.toString() + " (" +
                List._ID.toString() + " INTEGER PRIMARY KEY, " +
                List.col1.toString() + " TEXT, " +
                List.col2.toString() + " INTEGER )"
    }

    init {
        Log.d("DataBase", "Constructor")
    }
}