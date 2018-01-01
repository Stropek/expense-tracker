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

        if (intent.extras?.containsKey(Constants.Category.Name) == true)
            mNameEditText.setText(intent.getStringExtra(Constants.Category.Name).toString())
        if (intent.extras?.containsKey(Constants.Category.Description) == true)
            mDescriptionEditText.setText(intent.getStringExtra(Constants.Category.Description).toString())
        if (intent.extras?.containsKey(Constants.Category.Type) == true) {
            val type = CategoryTypes.valueOf(intent.getStringExtra(Constants.Category.Type))

            if (type == CategoryTypes.INCOME)
                mIncomeRadioButton.isChecked = true
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
        // TODO: change to save or add, depending on whether it's new category or edited one

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

        contentResolver.insert(ExpenseContract.ExpenseCategory.CONTENT_URI, values)

        // finish activity and return back to the list of categories
        finish()
    }
}
