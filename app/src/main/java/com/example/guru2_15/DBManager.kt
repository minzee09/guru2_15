package com.example.guru2_15

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL("CREATE TABLE userInfo (UserEmail text,  UID text primary key );")
        db!!.execSQL("CREATE TABLE schedule ( Sname text, Scolor text, Sdate text, SShour text, SSminute text, " +
                "SEhour text, SEminute text, Splace text, Smemo text, UID text, FOREIGN KEY(UID) REFERENCES userInfo(UID));")
       // db!!.execSQL("CREATE TABLE userInfo ( UserEmail text, FOREIGN KEY("") );")
      //  db!!.execSQL("CREATE TABLE userInfo (UserEmail text, UID text, FOREIGN KEY(UID) REFERENCES schedule(UID) )")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}