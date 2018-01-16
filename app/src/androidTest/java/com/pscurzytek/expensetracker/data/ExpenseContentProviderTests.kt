package com.pscurzytek.expensetracker.data

import android.content.ComponentName
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager.NameNotFoundException
import android.database.ContentObserver
import android.database.SQLException
import android.net.Uri
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.pscurzytek.expensetracker.CategoryTypes

import junit.framework.Assert.fail
import junit.framework.Assert.assertEquals

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import java.time.temporal.TemporalAmount

/**
 * Created by p.s.curzytek on 12/10/2017.
 */
@RunWith(AndroidJUnit4::class)
class ExpenseContentProviderTests {
    /* context used to access various parts of the system */
    private val mContext = InstrumentationRegistry.getTargetContext()

    @Before fun setUp() {
        val dbHelper = ExpenseTrackerDbHelper(mContext)
        val database = dbHelper.writableDatabase

        // purge the database
        database.delete(ExpenseContract.ExpenseCategory.TABLE_NAME, null, null)
        database.delete(ExpenseContract.ExpenseEntry.TABLE_NAME, null, null)
    }

    @get:Rule val thrown: ExpectedException = ExpectedException.none()

    @Test fun provider_is_registered_correctly() {
        val packageName = mContext.packageName
        val expenseProviderClassName = ExpenseContentProvider::class.java.name
        val componentName = ComponentName(packageName, expenseProviderClassName)

        try {
            val providerInfo = mContext.packageManager.getProviderInfo(componentName, 0)
            val actualAuthority = providerInfo.authority

            assertEquals("Error: content provider registered with authority '$actualAuthority' instead of expected '$packageName",
                    actualAuthority,
                    packageName)
        }
        catch(ex: NameNotFoundException) {
            fail("Error: content provider not registered at $packageName")
        }
    }

    @Test fun uri_matcher_expense_entries_uris_are_configured_correctly() {
        val uriMatcher = ExpenseContentProvider.buildUriMatcher()

        assertEquals("ERROR: The expense entries URI was matched incorrectly",
                ExpenseContentProvider.EXPENSE_ENTRIES,
                uriMatcher.match(ExpenseContract.ExpenseEntry.CONTENT_URI))

        assertEquals("ERROR: The expense entry URI with ID was matched incorrectly",
                ExpenseContentProvider.EXPENSE_ENTRY_WITH_ID,
                uriMatcher.match(ExpenseContract.ExpenseEntry.CONTENT_URI.buildUpon().appendPath("1").build()))
    }
    @Test fun uri_matcher_categories_uris_are_configured_correctly() {
        val uriMatcher = ExpenseContentProvider.buildUriMatcher()

        assertEquals("ERROR: The expense categories URI was matched incorrectly",
                ExpenseContentProvider.EXPENSE_CATEGORIES,
                uriMatcher.match(ExpenseContract.ExpenseCategory.CONTENT_URI))

        assertEquals("ERROR: The expense category URI with ID was matched incorrectly",
                ExpenseContentProvider.EXPENSE_CATEGORY_WITH_ID,
                uriMatcher.match(ExpenseContract.ExpenseCategory.CONTENT_URI.buildUpon().appendPath("1").build()))
    }

    @Test fun insert_uknown_uri_should_throw_unsupported_operation_exception() {
        thrown.expect(UnsupportedOperationException::class.java)

        val unknownUri = ExpenseContract.BASE_CONTENT_URI.buildUpon().appendPath("unknown").build()

        setObservedUriOnContentResolver(mContext.contentResolver, unknownUri, TestUtilities.testContentObserver)

        mContext.contentResolver.insert(unknownUri, ContentValues())
    }
    @Test fun insert_to_categories_with_valid_parameters_should_succeed() {
        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseCategory.CONTENT_URI

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        val expectedUri = ExpenseContract.ExpenseCategory.CONTENT_URI.buildUpon().appendPath("1").build()
        val actualUri = insertCategory(contentResolver, uri, "name", CategoryTypes.EXPENSE, "YYYY-MM-DD", "description")

        assertEquals("Unable to insert item through provider", expectedUri, actualUri)

        contentObserver.waitForNotificationOrFail()
        contentResolver.unregisterContentObserver(contentObserver)
    }
    @Test fun insert_to_categories_multiple_with_valid_parameters_should_succeed() {
        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseCategory.CONTENT_URI

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        val expectedUri_1 = ExpenseContract.ExpenseCategory.CONTENT_URI.buildUpon().appendPath("1").build()
        val expectedUri_2 = ExpenseContract.ExpenseCategory.CONTENT_URI.buildUpon().appendPath("2").build()
        val expectedUri_3 = ExpenseContract.ExpenseCategory.CONTENT_URI.buildUpon().appendPath("3").build()

        val actualUri_1 = insertCategory(contentResolver, uri, "name", CategoryTypes.EXPENSE, "YYYY-MM-DD", "description")
        val actualUri_2 = insertCategory(contentResolver, uri, "name", CategoryTypes.INCOME, "YYYY-MM-DD", "description")
        val actualUri_3 = insertCategory(contentResolver, uri, "otherName", CategoryTypes.EXPENSE, "YYYY-MM-DD", "description")

        assertEquals("Unable to insert item through provider", expectedUri_1, actualUri_1)
        assertEquals("Unable to insert item through provider", expectedUri_2, actualUri_2)
        assertEquals("Unable to insert item through provider", expectedUri_3, actualUri_3)

        contentObserver.waitForNotificationOrFail()
        contentResolver.unregisterContentObserver(contentObserver)
    }
    @Test fun insert_to_categories_duplicate_name_and_type_should_throw_sql_exception() {
        thrown.expect(SQLException::class.java)

        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseCategory.CONTENT_URI

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        insertCategory(contentResolver, uri, "name", CategoryTypes.EXPENSE, "2000-10-10", "description one")
        insertCategory(contentResolver, uri, "name", CategoryTypes.EXPENSE, "1900-10-10", "description two")
    }
    @Test fun insert_to_categories_with_invalid_parameters_should_throw_sql_exception() {
        thrown.expect(SQLException::class.java)

        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseCategory.CONTENT_URI

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        val contentValues = ContentValues()
        contentValues.put("incorrect_column_name", "value")

        mContext.contentResolver.insert(uri, contentValues)
    }
    @Test fun insert_to_expenses_with_valid_parameters_should_succeed() {
        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseEntry.CONTENT_URI

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        val expectedUri = ExpenseContract.ExpenseEntry.CONTENT_URI.buildUpon().appendPath("1").build()
        val actualUri = insertExpense(contentResolver, uri, "name", CategoryTypes.EXPENSE, "general", 1000, "YYYY-MM-DD")

        assertEquals("Unable to insert item through provider", expectedUri, actualUri)

        contentObserver.waitForNotificationOrFail()
        contentResolver.unregisterContentObserver(contentObserver)
    }
    @Test fun insert_to_expenses_with_invalid_parameters_should_throw_sql_exception() {
        thrown.expect(SQLException::class.java)

        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseEntry.CONTENT_URI

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        val contentValues = ContentValues()
        contentValues.put("incorrect_column_name", "value")

        mContext.contentResolver.insert(uri, contentValues)
    }

    @Test fun query_with_uknown_uri_should_throw_unsupported_operation_exception() {
        thrown.expect(UnsupportedOperationException::class.java)

        val unknownUri = ExpenseContract.BASE_CONTENT_URI.buildUpon().appendPath("unknown").build()

        setObservedUriOnContentResolver(mContext.contentResolver, unknownUri, TestUtilities.testContentObserver)

        mContext.contentResolver.query(unknownUri, null, null, null, null)
    }
    @Test fun query_with_categories_content_uri_returns_all_categories() {
        // given
        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseCategory.CONTENT_URI

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        for (i in 0..2) {
            insertCategory(contentResolver, uri, "income_$i", CategoryTypes.INCOME)
        }
        for (i in 2 downTo 0) {
            insertCategory(contentResolver, uri, "expense_$i", CategoryTypes.EXPENSE)
        }

        // when
        val categories = contentResolver.query(uri, null, null, null, null)

        // then
        val nameIndex = categories.getColumnIndex(ExpenseContract.ExpenseCategory.COLUMN_NAME)

        assertEquals("Unexpected number of categories", categories.count, 6)
        categories.moveToFirst()
        assertEquals("income_0", categories.getString(nameIndex))
        categories.moveToPosition(3)
        assertEquals("expense_2", categories.getString(nameIndex))

        categories.close()
    }
    @Test fun query_with_category_content_by_id_uri_returns_category_with_given_id() {
        // given
        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseCategory.CONTENT_URI
        val detailsUri = uri.buildUpon().appendPath("3").build()

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        for (i in 0..9) {
            insertCategory(contentResolver, uri, "category $i", CategoryTypes.INCOME, description = "desc $i")
        }

        // when
        val category = contentResolver.query(detailsUri, null, null, null, null)

        // then
        category.moveToFirst()
        assertEquals(1, category.count)
        assertEquals(3, category.getInt(category.getColumnIndex(ExpenseContract.ExpenseCategory.ID)))
    }
    @Test fun query_with_expenses_content_uri_returns_all_expenses() {
        // given
        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseEntry.CONTENT_URI

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        for (i in 0..2) {
            insertExpense(contentResolver, uri, "income_$i", CategoryTypes.EXPENSE, "category", i * 1000, "YYYY-MM-DD")
        }
        for (i in 2 downTo 0) {
            insertExpense(contentResolver, uri, "expense_$i", CategoryTypes.INCOME, "category", i * 1000, "YYYY-MM-DD")
        }

        // when
        val expenses = contentResolver.query(uri, null, null, null, null)

        // then
        val nameIndex = expenses.getColumnIndex(ExpenseContract.ExpenseEntry.COLUMN_NAME)

        assertEquals("Unexpected number of categories", expenses.count, 6)
        expenses.moveToFirst()
        assertEquals("income_0", expenses.getString(nameIndex))
        expenses.moveToPosition(3)
        assertEquals("expense_2", expenses.getString(nameIndex))

        expenses.close()
    }
    @Test fun query_with_expense_content_by_id_uri_returns_expense_with_given_id() {
        // TODO:
    }

    @Test fun update_with_uknown_uri_should_throw_unsupported_operation_exception() {
        thrown.expect(UnsupportedOperationException::class.java)

        val unknownUri = ExpenseContract.BASE_CONTENT_URI.buildUpon().appendPath("unknown").build()

        setObservedUriOnContentResolver(mContext.contentResolver, unknownUri, TestUtilities.testContentObserver)

        mContext.contentResolver.update(unknownUri, ContentValues(), null, null)
    }
    @Test fun update_with_category_content_by_id_uri_updates_exsiting_category() {
        // given
        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseCategory.CONTENT_URI

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        val existingUri = insertCategory(contentResolver, uri, "category before", CategoryTypes.INCOME, description = "desc")

        // when
        val updated = updateCategory(contentResolver, existingUri, "category after", CategoryTypes.EXPENSE, description = "desc after")

        // then
        val updatedCategory = contentResolver.query(existingUri, null, null, null)
        updatedCategory.moveToFirst()
        assertEquals(1, updated)
        assertEquals(1, updatedCategory.count)
    }
    @Test fun update_with_expense_content_by_id_uri_updates_exsiting_category() {

    }

    @Test fun delete_with_uknown_uri_should_throw_unsupported_operation_exception() {
        thrown.expect(UnsupportedOperationException::class.java)

        val unknownUri = ExpenseContract.BASE_CONTENT_URI.buildUpon().appendPath("unknown").build()

        setObservedUriOnContentResolver(mContext.contentResolver, unknownUri, TestUtilities.testContentObserver)

        mContext.contentResolver.delete(unknownUri, null, null)
    }
    @Test fun delete_with_existing_id_removes_category_from_the_database() {
        // given
        val contentResolver = mContext.contentResolver
        val contentObserver = TestUtilities.testContentObserver
        val uri = ExpenseContract.ExpenseCategory.CONTENT_URI
        val existingUri = uri.buildUpon().appendPath("2").build()

        setObservedUriOnContentResolver(contentResolver, uri, contentObserver)

        for (i in 0..5) {
            insertCategory(contentResolver, uri, "category $i", CategoryTypes.INCOME, description = "desc $i")
        }

        // when
        val deleted = contentResolver.delete(existingUri, null, null)

        // then
        val categories = contentResolver.query(uri, null, null, null, null)
        assertEquals(1, deleted)
        assertEquals(5, categories.count)
    }
    @Test fun delete_with_existing_id_removes_expense_from_the_database() {

    }

    private fun setObservedUriOnContentResolver(contentResolver: ContentResolver, uri: Uri?, contentObserver: ContentObserver) {
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                uri,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                contentObserver)
    }

    private fun insertCategory(contentResolver: ContentResolver, uri: Uri, name: String, type: CategoryTypes, date: String = "", description: String? = ""): Uri {
        val contentValues = ContentValues()
        contentValues.put(ExpenseContract.ExpenseCategory.COLUMN_NAME, name)
        contentValues.put(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION, description)
        contentValues.put(ExpenseContract.ExpenseCategory.COLUMN_TYPE, type.name)
        contentValues.put(ExpenseContract.ExpenseCategory.COLUMN_CREATED, date)

        return contentResolver.insert(uri, contentValues)
    }
    private fun insertExpense(contentResolver: ContentResolver, uri: Uri, name: String, type: CategoryTypes, category: String, amount: Int, date: String = ""): Uri {
        val contentValues = ContentValues()
        contentValues.put(ExpenseContract.ExpenseEntry.COLUMN_NAME, name)
        contentValues.put(ExpenseContract.ExpenseEntry.COLUMN_TYPE, type.name)
        contentValues.put(ExpenseContract.ExpenseEntry.COLUMN_CATEGORY, category)
        contentValues.put(ExpenseContract.ExpenseEntry.COLUMN_AMOUNT, amount)
        contentValues.put(ExpenseContract.ExpenseEntry.COLUMN_CREATED, date)

        return contentResolver.insert(uri, contentValues)
    }

    private fun updateCategory(contentResolver: ContentResolver, uri: Uri, name: String, type: CategoryTypes, date: String = "", description: String? =""): Int {
        val contentValues = ContentValues()
        contentValues.put(ExpenseContract.ExpenseCategory.COLUMN_NAME, name)
        contentValues.put(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION, description)
        contentValues.put(ExpenseContract.ExpenseCategory.COLUMN_TYPE, type.name)
        contentValues.put(ExpenseContract.ExpenseCategory.COLUMN_CREATED, date)

        return contentResolver.update(uri, contentValues, null, null)
    }
}