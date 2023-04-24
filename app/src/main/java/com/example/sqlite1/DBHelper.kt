package com.example.sqlite1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "users.db"
        private const val TABLE_NAME = "users"
        private const val COLUMN_EMP_ID = "emp_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_SALARY = "salary"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_EMP_ID INTEGER PRIMARY KEY,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_SALARY INTEGER" + ")")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(user: UserModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_EMP_ID, user.empId)
        values.put(COLUMN_NAME, user.name)
        values.put(COLUMN_SALARY, user.salary)
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id != -1L
    }


    fun getAllUsers(): List<UserModel> {
        val userList = mutableListOf<UserModel>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val empIdIndex = cursor.getColumnIndex(COLUMN_EMP_ID)
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
            val salaryIndex = cursor.getColumnIndex(COLUMN_SALARY)

            do {
                val empId = if (empIdIndex >= 0) cursor.getInt(empIdIndex) else 0
                val name = if (nameIndex >= 0) cursor.getString(nameIndex) else ""
                val salary = if (salaryIndex >= 0) cursor.getInt(salaryIndex) else 0
                val user = UserModel(empId, name, salary)
                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    fun updateUser(user: UserModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, user.name)
        values.put(COLUMN_SALARY, user.salary)
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_EMP_ID = ?", arrayOf(user.empId.toString()))
        db.close()
        return rowsAffected > 0
    }


    fun deleteUser(user: UserModel): Boolean {
        val db = this.writableDatabase
        val rowsAffected = db.delete(TABLE_NAME, "$COLUMN_EMP_ID = ?", arrayOf(user.empId.toString()))
        db.close()
        return rowsAffected > 0
    }

}
