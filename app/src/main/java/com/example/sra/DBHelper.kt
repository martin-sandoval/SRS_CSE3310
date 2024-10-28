package com.example.sra
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        dropTable(db)
        createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun dropTable(db: SQLiteDatabase){
        db.execSQL("DROP TABLE  IF EXISTS " + TABLE_NAME)
    }

    fun createTable(db: SQLiteDatabase){
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                USERNAME_COl + " TEXT," +
                PASSWORD_COL + " TEXT" + ")")
        db.execSQL(query)
    }

    fun addName(username : String, password : String ){
        val values = ContentValues()
        values.put(USERNAME_COl, username)
        values.put(PASSWORD_COL, password)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllUsers(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    fun getUser(username:String, password: String): Cursor? {
        if (username =="" && password == "")
            return null
        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME + " WHERE username = '" + username + "' AND password = '"+password + "'"
        Log.i("myapp", query)
        return db.rawQuery( query, null)
    }
    companion object{
        val DATABASE_NAME = "CSE3310"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "users"
        val ID_COL = "id"
        val USERNAME_COl = "username"
        val PASSWORD_COL = "password"
    }
}
