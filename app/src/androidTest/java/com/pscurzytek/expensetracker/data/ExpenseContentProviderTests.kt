package com.pscurzytek.expensetracker.data

import android.content.ComponentName
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import junit.framework.Assert.fail
import junit.framework.Assert.assertEquals

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith

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
    @Test fun uri_matcher_expense_categories_uris_are_configured_correctly() {
        val uriMatcher = ExpenseContentProvider.buildUriMatcher()

        assertEquals("ERROR: The expense categories URI was matched incorrectly",
                ExpenseContentProvider.EXPENSE_CATEGORIES,
                uriMatcher.match(ExpenseContract.ExpenseCategory.CONTENT_URI))

        assertEquals("ERROR: The expense category URI with ID was matched incorrectly",
                ExpenseContentProvider.EXPENSE_CATEGORY_WITH_ID,
                uriMatcher.match(ExpenseContract.ExpenseCategory.CONTENT_URI.buildUpon().appendPath("1").build()))
    }

    @Test fun insert_to_uknown_uri_should_throw_unsupported_operation_exception() {
        thrown.expect(UnsupportedOperationException::class.java)

        val unknownUri = ExpenseContract.BASE_CONTENT_URI.buildUpon().appendPath("unknown").build()

        setObservedUriOnContentResolver(mContext.contentResolver, unknownUri)

        mContext.contentResolver.insert(unknownUri, ContentValues())
    }
    @Test fun insert_to_expense_categories_with_valid_parameters_should_succeed() {

    }
    @Test fun insert_to_expense_categories_with_invalid_parameters_should_throw_sql_exception() {

    }

    private fun setObservedUriOnContentResolver(contentResolver: ContentResolver, uri: Uri?) {
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                uri,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                TestUtilities.testContentObserver)
    }
}