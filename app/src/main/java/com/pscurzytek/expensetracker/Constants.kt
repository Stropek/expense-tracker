package com.pscurzytek.expensetracker

/**
 * Created by p.s.curzytek on 1/1/2018.
 */
class Constants {
    companion object {
        const val ID = "id"
        const val SortOrder = "sortOrder"
    }

    class CategoryProperties {
        companion object {
            const val Name = "category_name"
            const val Description = "category_desc"
            const val Type = "category_type"
        }
    }

    class ExpenseProperties {
        companion object {
            const val Name = "expense_name"
            const val Date = "expense_date"
            const val Category = "expense_category"
            const val Type = "expense_type"
            const val Amount = "expense_amount"
        }
    }
}