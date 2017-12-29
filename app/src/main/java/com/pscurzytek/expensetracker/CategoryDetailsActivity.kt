package com.pscurzytek.expensetracker

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.pscurzytek.expensetracker.data.ExpenseContract
import java.text.SimpleDateFormat
import java.util.*

class CategoryDetailsActivity : AppCompatActivity() {
    private var mType = "Expense"
    private lateinit var mNameEditText: EditText
    private lateinit var mDescriptionEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mNameEditText = findViewById(R.id.et_category_name)
        mDescriptionEditText = findViewById(R.id.et_category_description)

        if (intent.extras?.containsKey("category_name") == true)
            mNameEditText.setText(intent.getStringExtra("category_name").toString())
        if (intent.extras?.containsKey("category_desc") == true)
            mDescriptionEditText.setText(intent.getStringExtra("category_desc").toString())
    }

    fun onTypeSelected(view: View) {
        if (findViewById<RadioButton>(R.id.rbtn_category_type_income).isChecked) {
            mType = getString(R.string.label_type_income)
        } else if (findViewById<RadioButton>(R.id.rbtn_category_type_expense).isChecked) {
            mType = getString(R.string.label_type_expense)
        }
    }

    fun onCategoryAdded(view: View) {
        // TODO: change to save or add, depending on whether it's new category or edited one

        val name = mNameEditText.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(this@CategoryDetailsActivity, "Name is required", Toast.LENGTH_SHORT).show()
            return
        }

        val values = ContentValues()
        values.put(ExpenseContract.ExpenseCategory.COLUMN_NAME, name)
        values.put(ExpenseContract.ExpenseCategory.COLUMN_TYPE, mType)

        val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)
        values.put(ExpenseContract.ExpenseCategory.COLUMN_CREATED, dateFormat.format(Date()))

        val description = (findViewById<EditText>(R.id.et_category_description)).text.toString()
        if (description.isNotEmpty()) {
            values.put(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION, description)
        }

        contentResolver.insert(ExpenseContract.ExpenseCategory.CONTENT_URI, values)

        // finish activity and return back to the list of categories
        finish()
    }
}
