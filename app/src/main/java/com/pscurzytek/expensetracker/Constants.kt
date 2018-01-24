package com.pscurzytek.expensetracker

/**
 * Created by p.s.curzytek on 1/1/2018.
 */
class Constants {
    companion object {
        val ID = "id"
    }

    class CategoryProperties {
        companion object {
            val Name = "category_name"
            val Description = "category_desc"
            val Type = "category_type"
        }
    }

    class ExpenseProperties {
        companion object {
            val Name = "expense_name"
            val Date = "expense_date"
            val Category = "expense_category"
            val Type = "expense_type"
        }
    }
}