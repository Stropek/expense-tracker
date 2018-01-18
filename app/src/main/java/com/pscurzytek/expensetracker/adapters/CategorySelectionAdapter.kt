package com.pscurzytek.expensetracker.adapters

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getIntByColumn
import com.pscurzytek.expensetracker.data.extensions.getStringByColumn

/**
 * Created by p.s.curzytek on 1/17/2018.
 */
class CategorySelectionAdapter(context: Context): RecyclerView.Adapter<CategorySelectionAdapter.CategoryViewHolder>() {
    private var mCursor: Cursor? = null
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup?, position: Int): CategoryViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_category_select, parent, false)

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val cursor = mCursor

        if (cursor != null) {
            cursor.moveToPosition(position)

            val id = cursor.getIntByColumn(ExpenseContract.ExpenseCategory.ID)
            val name = cursor.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_NAME)
//            val type = cursor.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_TYPE)
//
            holder.itemView.tag = id
            holder.tvCategoryName.text = name
//
//            val expenseColor = ResourcesCompat.getColor(mContext!!.resources, R.color.materialRed, null)
//            val incomeColor = ResourcesCompat.getColor(mContext!!.resources, R.color.materialGreen, null)
//            val bckgColor = if (type.toLowerCase() == "income") incomeColor else expenseColor
//            (holder.tvCategoryName.parent as LinearLayout).setBackgroundColor(bckgColor)
        }
    }

    override fun getItemCount(): Int {
        return mCursor?.count ?: 0
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

    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.tv_category_name)
    }
}