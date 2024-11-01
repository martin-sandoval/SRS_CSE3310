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
        db.execSQL("PRAGMA foreign_keys=ON;")
        dropTable(db)
        createTable(db)
        createStocksTable(db)//
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKS)
        onCreate(db)
    }

    fun dropTable(db: SQLiteDatabase){
        db.execSQL("DROP TABLE  IF EXISTS " + TABLE_NAME)
    }

    fun createTable(db: SQLiteDatabase){
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER, " +
                USERNAME_COl + " TEXT PRIMARY KEY," +
                PASSWORD_COL + " TEXT" + ")")
        db.execSQL(query)
    }

    fun createStocksTable(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_STOCKS + " ("
                + STOCK_ID_COL + " INTEGER PRIMARY KEY, "
                + USERNAME_COl + " TEXT, "  // Foreign key to link to users
                + STOCK_NAME_COL + " TEXT, "
                + PURCHASE_DATE_COL + " TEXT, "
                + STOCK_AMOUNT + " INTEGER, "
                + "FOREIGN KEY (" + USERNAME_COl + ") REFERENCES " + TABLE_NAME + "(" + USERNAME_COl + ")" + ")") // Link to users table
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
    fun getUserName(username:String): Cursor? {
        if (username =="")
            return null
        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME + " WHERE username = '" + username+ "'"
        Log.i("myapp", query)
        return db.rawQuery( query, null)
    }

    fun addStock(username: String, stockName: String, purchaseDate: String, stockAmount :Int) {
        val values = ContentValues()
        values.put(USERNAME_COl, username)
        values.put(STOCK_NAME_COL, stockName)
        values.put(PURCHASE_DATE_COL, purchaseDate)
        values.put(STOCK_AMOUNT, stockAmount)
        val db = this.writableDatabase
        db.insert(TABLE_STOCKS, null, values)
        db.close()
    }
    fun getStocksByUser(username: String): Cursor? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_STOCKS WHERE username = '" + username+"'"
        return db.rawQuery(query,null)
    }
    fun searchStock(username: String, stockName: String): Cursor? {
        if(stockName == "")
            return null
        val db = this.readableDatabase
        //val query = "SELECT * FROM $TABLE_STOCKS WHERE $STOCK_NAME_COL = ? AND $USERNAME_COl = ?"
        val query = "SELECT * FROM $TABLE_STOCKS WHERE $STOCK_NAME_COL LIKE ? AND $USERNAME_COl = ?"


        Log.i("myapp", query)
        //return db.rawQuery( query, arrayOf(stockName, username))
        return db.rawQuery(query, arrayOf("%$stockName%", username))

    }

    companion object{
        val DATABASE_NAME = "CSE3310"
        private val DATABASE_VERSION = 6
        val TABLE_NAME = "users"
        val ID_COL = "id"
        val USERNAME_COl = "username"
        val PASSWORD_COL = "password"
        const val STOCK_AMOUNT = "stockAmount"
        const val TABLE_STOCKS = "stocks"
        const val STOCK_ID_COL = "stock_id"
        const val USER_ID_COL = "user_id"  // Foreign key referencing users
        const val STOCK_NAME_COL = "stock_name"
        const val PURCHASE_DATE_COL = "purchase_date"
    }
}
