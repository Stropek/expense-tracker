package com.pscurzytek.expensetracker.helpers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat

/**
 * Created by p.s.curzytek on 1/21/2018.
 */
class DecimalTextWatcher(private val editText: EditText, private val decimalPlaces: Int = 2): TextWatcher {
    private val decimalFormat = DecimalFormat("#,###")
    private val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator

    override fun afterTextChanged(sequence: Editable) {
        editText.removeTextChangedListener(this)

        if (sequence.contains(decimalSeparator)) {
            val excessiveDigits = sequence.indexOf(decimalSeparator)  + 1 - sequence.length + decimalPlaces
            if (excessiveDigits < 0) {
                val newValue = sequence.subSequence(0, sequence.length + excessiveDigits)
                editText.setText(newValue)
                editText.setSelection(newValue.length)
            }
        }

        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) { }
}