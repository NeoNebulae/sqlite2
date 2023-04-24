package com.example.sqlite1

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var userListView: ListView
    private lateinit var userList: ArrayList<UserModel>
    private lateinit var adapter: ArrayAdapter<UserModel>
    private lateinit var emptyListTextView: TextView
    private lateinit var addUserButton: Button
    private lateinit var readUserButton: Button
    private lateinit var updateUserButton: Button
    private lateinit var deleteUserButton: Button
    private lateinit var editTextEmpId: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextSalary: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        userListView = findViewById(R.id.userListView)
        userList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userList)
        userListView.adapter = adapter
        emptyListTextView = findViewById(R.id.emptyListTextView)
        addUserButton = findViewById(R.id.addUserButton)
        readUserButton = findViewById(R.id.readUserButton)
        updateUserButton = findViewById(R.id.updateUserButton)
        deleteUserButton = findViewById(R.id.deleteUserButton)
        editTextEmpId = findViewById(R.id.editTextEmpId)
        editTextName = findViewById(R.id.editTextName)
        editTextSalary = findViewById(R.id.editTextSalary)

        addUserButton.setOnClickListener {
            val empId = editTextEmpId.text.toString().toInt()
            val name = editTextName.text.toString()
            val salary = editTextSalary.text.toString().toInt()
            val user = UserModel(empId, name, salary)
            if (dbHelper.insertUser(user)) {
                userList.add(user)
                adapter.notifyDataSetChanged()
                editTextEmpId.setText("")
                editTextName.setText("")
                editTextSalary.setText("")
                emptyListTextView.visibility = View.GONE
            } else {
                Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
            }
        }

        readUserButton.setOnClickListener {
            userList.clear()
            userList.addAll(dbHelper.getAllUsers())
            adapter.notifyDataSetChanged()
            if (userList.isEmpty()) {
                emptyListTextView.visibility = View.VISIBLE
            } else {
                emptyListTextView.visibility = View.GONE
            }
        }


        userListView.setOnItemClickListener { _, _, position, _ ->
            val user = userList[position]
            editTextEmpId.setText(user.empId.toString())
            editTextName.setText(user.name)
            editTextSalary.setText(user.salary.toString())
        }

        updateUserButton.setOnClickListener {
            val empId = editTextEmpId.text.toString().toInt()
            val name = editTextName.text.toString()
            val salary = editTextSalary.text.toString().toInt()

            // find the original user object from the list
            val originalUser = userList.find { it.empId == empId }

            originalUser?.name = name
            originalUser?.salary = salary

            if (originalUser != null && dbHelper.updateUser(originalUser)) {
                adapter.notifyDataSetChanged()
                editTextEmpId.setText("")
                editTextName.setText("")
                editTextSalary.setText("")
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show()
            }
        }

        deleteUserButton.setOnClickListener {
            val empId = editTextEmpId.text.toString().toInt()
            val user = UserModel(empId, "", 0)
            if (dbHelper.deleteUser(user)) {
                userList.remove(user)
                adapter.notifyDataSetChanged()
                editTextEmpId.setText("")
                editTextName.setText("")
                editTextSalary.setText("")
            } else {
                Toast.makeText(this, "Failed to delete student", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
