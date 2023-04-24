package com.example.sqlite1

class UserModel(var empId: Int, var name: String, var salary: Int) {

    companion object {
        const val TABLE_NAME = "users"
        const val COLUMN_EMP_ID = "empId"
        const val COLUMN_NAME = "name"
        const val COLUMN_SALARY = "salary"

        const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_EMP_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_SALARY INTEGER)"
    }

    override fun toString(): String {
        return "Emp Id: $empId\nName: $name\nSalary: $salary"
    }

}
