package com.pscurzytek.expensetracker.adapters

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.extensions.getDayAndMonth
import com.pscurzytek.expensetracker.data.extensions.getExpense
import com.pscurzytek.expensetracker.data.extensions.getYear
import com.pscurzytek.expensetracker.interfaces.ViewHolderWithForeground

/**
 * Created by p.s.curzytek on 1/15/2018.
 */
class ExpenseListAdapter(context: Context?): RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>() {
    private var mCursor: Cursor? = null
    private var mContext = context

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val cursor = mCursor

        if (cursor != null) {
            cursor.moveToPosition(position)

            val expense = cursor.getExpense()

            holder.itemView.tag = expense.id
            holder.tvExpenseName.text = expense.name
            holder.tvDayAndMonth.text = expense.date?.getDayAndMonth()
            holder.tvYear.text = expense.date?.getYear()
            holder.tvCategoryName.text = expense.category.take(1)
            holder.tvAmount.text = "$${(expense.amount / 100.0)}"
        }
    }

    override fun getItemCount(): Int {
        return mCursor?.count ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_expense, parent, false)

        return ExpenseViewHolder(view)
    }

    fun swapCursor(newCursor: Cursor?): Cursor? {
        if (mCursor == newCursor)
            return null

        val temp = mCursor
        mCursor = newCursor

        if (newCursor != null)
            notifyDataSetChanged()

        return temp
    }

    class ExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), ViewHolderWithForeground {
        var tvExpenseName: TextView = itemView.findViewById(R.id.tv_expense_name)
        var tvDayAndMonth: TextView = itemView.findViewById(R.id.tv_expense_dayAndMonth)
        var tvYear: TextView = itemView.findViewById(R.id.tv_expense_year)
        var tvCategoryName: TextView = itemView.findViewById(R.id.tv_expense_category)
        var tvAmount: TextView = itemView.findViewById(R.id.tv_expense_amount)

        override var viewForeground = itemView.findViewById<RelativeLayout>(R.id.rl_foreground)
    }
}