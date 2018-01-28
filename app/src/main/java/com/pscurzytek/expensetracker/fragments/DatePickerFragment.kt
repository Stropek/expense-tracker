package com.pscurzytek.expensetracker.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.extensions.getDate
import com.pscurzytek.expensetracker.data.extensions.getString
import java.time.LocalDate
import java.util.*

/**
 * Created by p.s.curzytek on 1/20/2018.
 */
class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = GregorianCalendar.getInstance()

        if (arguments?.containsKey(Constants.ExpenseProperties.Date) == true) {
            val dateString = arguments.getString(Constants.ExpenseProperties.Date)
            calendar.time = dateString.getDate()
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val dateEditText = activity.findViewById<EditText>(R.id.et_date)
        val date = LocalDate.of(year, month + 1, dayOfMonth)
        dateEditText.setText(date.getString())
    }
}