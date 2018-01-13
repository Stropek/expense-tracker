package com.pscurzytek.expensetracker.activities

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.ExpenseContract
import java.text.SimpleDateFormat
import java.util.*

class CategoryDetailsActivity : AppCompatActivity() {
    private var mType = CategoryTypes.EXPENSE
    private lateinit var mNameEditText: EditText
    private lateinit var mDescriptionEditText: EditText
    private lateinit var mIncomeRadioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mNameEditText = findViewById(R.id.et_category_name)
        mDescriptionEditText = findViewById(R.id.et_category_description)
        mIncomeRadioButton =findViewById(R.id.rbtn_category_type_income)

        if (intent.extras?.containsKey(Constants.CategoryProperties.Name) == true)
            mNameEditText.setText(intent.getStringExtra(Constants.CategoryProperties.Name).toString())
        if (intent.extras?.containsKey(Constants.CategoryProperties.Description) == true)
            mDescriptionEditText.setText(intent.getStringExtra(Constants.CategoryProperties.Description).toString())
        if (intent.extras?.containsKey(Constants.CategoryProperties.Type) == true) {
            val type = CategoryTypes.valueOf(intent.getStringExtra(Constants.CategoryProperties.Type))
            mType = type
            if (type == CategoryTypes.INCOME) {
                mIncomeRadioButton.isChecked = true
            }
        }
    }

    fun onTypeSelected(view: View) {
        if (findViewById<RadioButton>(R.id.rbtn_category_type_income).isChecked) {
            mType = CategoryTypes.valueOf(getString(R.string.label_type_income).toUpperCase())
        } else if (findViewById<RadioButton>(R.id.rbtn_category_type_expense).isChecked) {
            mType = CategoryTypes.valueOf(getString(R.string.label_type_expense).toUpperCase())
        }
    }

    fun onCategorySave(view: View) {
        val name = mNameEditText.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(this@CategoryDetailsActivity, "Name is required", Toast.LENGTH_SHORT).show()
            return
        }

        val values = ContentValues()
        values.put(ExpenseContract.ExpenseCategory.COLUMN_NAME, name)
        values.put(ExpenseContract.ExpenseCategory.COLUMN_TYPE, mType.toString())

        val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)
        values.put(ExpenseContract.ExpenseCategory.COLUMN_CREATED, dateFormat.format(Date()))

        val description = (findViewById<EditText>(R.id.et_category_description)).text.toString()
        if (description.isNotEmpty()) {
            values.put(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION, description)
        }

        if (intent.extras != null && intent.extras.containsKey(Constants.ID)) {
            val id = intent.extras.getString(Constants.ID)
            val updateUri = ExpenseContract.ExpenseCategory.CONTENT_URI
                    .buildUpon().appendPath(id).build()
            contentResolver.update(updateUri, values, null, null)
        } else {
            contentResolver.insert(ExpenseContract.ExpenseCategory.CONTENT_URI, values)
        }

        // finish activity and return back to the list of categories
        finish()
    }
}
