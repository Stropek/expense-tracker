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

    class ExpenseCategoryTable {
        class Columns {
            companion object {
                val Name = "name"
                val Description = "description"
                val Type = "type"
                val Created = "created"
            }
        }
        companion object {
            val Name = "categories"
        }
    }
}