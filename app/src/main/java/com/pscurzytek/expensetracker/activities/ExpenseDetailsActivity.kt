package com.pscurzytek.expensetracker.activities

import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getString
import com.pscurzytek.expensetracker.data.extensions.getStringByColumn
import com.pscurzytek.expensetracker.databinding.ActivityExpenseDetailsBinding
import com.pscurzytek.expensetracker.fragments.DatePickerFragment
import com.pscurzytek.expensetracker.helpers.DecimalTextWatcher
import java.time.LocalDate

class ExpenseDetailsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityExpenseDetailsBinding
    private lateinit var mCategories: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_expense_details)

        mBinding.etName.setText(intent.getStringExtra(Constants.ExpenseProperties.Name))
        mBinding.etDate.setText(LocalDate.now().getString())
        mBinding.tvCategory.text = intent.extras.getString(Constants.ExpenseProperties.Category)
        val type = intent.extras.getSerializable(Constants.ExpenseProperties.Type) as CategoryTypes
        mBinding.tvType.text = type.name
        val amount = intent.extras.getDouble(Constants.ExpenseProperties.Amount)
        if (amount > 0.0) {
            mBinding.etAmount.setText(amount.toString())
        }

        mBinding.etAmount.addTextChangedListener(DecimalTextWatcher(mBinding.etAmount))

        mCategories = getCategories(type)
    }

    fun showDatePickerDialog(view: View) {
        val datePicker = DatePickerFragment()

        datePicker.arguments = Bundle()
        datePicker.arguments.putString(Constants.ExpenseProperties.Date, mBinding.etDate.text.toString())

        datePicker.show(fragmentManager, "datePicker")
    }

    fun showCategoryDialog(view: View) {
        val builder = AlertDialog.Builder(this)
                .setTitle(R.string.label_category)
                .setItems(mCategories) { dialog, which ->
                    mBinding.tvCategory.text = mCategories[which]
                }

        builder.show()
    }

    fun onExpenseSave(view: View) {
        val amount = mBinding.etAmount.text.toString()
        if (amount.isEmpty()) {
            Toast.makeText(this, "Amount is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (amount.toDouble() == 0.0) {
            Toast.makeText(this, "Amount must be positive", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val values = ContentValues()
            values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME, mBinding.etName.text.toString())
            values.put(ExpenseContract.ExpenseEntry.COLUMN_CATEGORY, mBinding.tvCategory.text.toString())
            values.put(ExpenseContract.ExpenseEntry.COLUMN_TYPE, mBinding.tvType.text.toString())
            values.put(ExpenseContract.ExpenseEntry.COLUMN_CREATED, mBinding.etDate.text.toString())
            // TODO: extract this conversion to INT to some helper class
            values.put(ExpenseContract.ExpenseEntry.COLUMN_AMOUNT, (mBinding.etAmount.text.toString().toDouble() * 100).toInt())

            if (intent.extras != null && intent.extras.containsKey(Constants.ID)) {
                val id = intent.extras.getString(Constants.ID)
                val updateUri = ExpenseContract.ExpenseEntry.CONTENT_URI
                        .buildUpon().appendPath(id).build()
                contentResolver.update(updateUri, values, null, null)
            } else {
                contentResolver.insert(ExpenseContract.ExpenseEntry.CONTENT_URI, values)
            }

            // finish activity and return all the way back to main activity
            finish()
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to insert expenses")
            ex.printStackTrace()
        }
    }

    private fun getCategories(type: CategoryTypes): Array<String> {
        var categories: Array<String> = arrayOf()

        val cursor = contentResolver.query(ExpenseContract.ExpenseCategory.CONTENT_URI,
                null,
                "${ExpenseContract.ExpenseCategory.COLUMN_TYPE}=?",
                arrayOf(type.name),
                null)

        while (cursor.moveToNext()) {
            categories = categories.plus(cursor.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_NAME))
        }

        return categories
    }

    companion object {
        val TAG = ExpenseDetailsActivity::class.java.simpleName
    }
}
