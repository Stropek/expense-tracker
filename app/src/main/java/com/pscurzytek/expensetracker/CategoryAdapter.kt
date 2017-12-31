package com.pscurzytek.expensetracker

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.pscurzytek.expensetracker.data.ExpenseContract

/**
 * Created by p.s.curzytek on 12/26/2017.
 */
class CategoryAdapter(context: Context): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var mCursor: Cursor? = null
    private var mContext = context

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val cursor = mCursor

        if (cursor != null) {
            val idIndex = cursor.getColumnIndex(ExpenseContract.ExpenseCategory.ID)
            val nameIndex = cursor.getColumnIndex(ExpenseContract.ExpenseCategory.COLUMN_NAME)
            val typeIndex = cursor.getColumnIndex(ExpenseContract.ExpenseCategory.COLUMN_TYPE)

            cursor.moveToPosition(position)

            val id = cursor.getInt(idIndex)
            val name = cursor.getString(nameIndex)
            val type = cursor.getString(typeIndex)

            holder.itemView.tag = id
            holder.tvCategoryName.text = name
            (holder.tvCategoryType.parent as LinearLayout).setBackgroundColor(if (type.toLowerCase() == "income") Color.GREEN else Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return mCursor?.count ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_category, parent, false)

        return CategoryViewHolder(view)
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
        var tvCategoryName: TextView = itemView.findViewById(R.id.tv_category_name)
        var tvCategoryType: TextView = itemView.findViewById(R.id.tv_category_type)
    }
}