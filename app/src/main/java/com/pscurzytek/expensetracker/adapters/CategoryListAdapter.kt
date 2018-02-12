package com.pscurzytek.expensetracker.adapters

import android.content.Context
import android.database.Cursor
import android.support.constraint.ConstraintLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getCategory
import com.pscurzytek.expensetracker.data.extensions.getIntByColumn
import com.pscurzytek.expensetracker.data.extensions.getStringByColumn
import com.pscurzytek.expensetracker.interfaces.ViewHolderWithForeground

/**
 * Created by p.s.curzytek on 12/26/2017.
 */
class CategoryListAdapter(context: Context?): RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {
    private var mCursor: Cursor? = null
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        // TODO: consider having just a single CategoryAdapter class which would load different layouts depending on ctor
        // or write a base adapter class which would provide default getItemCount & swapCursor
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_category, parent, false)

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val cursor = mCursor

        if (cursor != null) {
            cursor.moveToPosition(position)

            val category = cursor.getCategory()

            holder.itemView.tag = category.id
            holder.tvCategoryName.text = category.name

            // TODO: move this and similar method from ExpenseListAdapter to an extension method
            val expenseColor = ResourcesCompat.getColor(mContext!!.resources, R.color.materialRed, null)
            val incomeColor = ResourcesCompat.getColor(mContext!!.resources, R.color.materialGreen, null)
            val bckgColor = if (category.type.toLowerCase() == "income") incomeColor else expenseColor
            (holder.tvCategoryName.parent as ConstraintLayout).setBackgroundColor(bckgColor)
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

    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), ViewHolderWithForeground {
        var tvCategoryName: TextView = itemView.findViewById(R.id.tv_category_name)
        override var viewForeground = itemView.findViewById<RelativeLayout>(R.id.rl_foreground)
    }
}