package com.pscurzytek.expensetracker.data

import android.content.ComponentName
import android.content.pm.PackageManager.NameNotFoundException
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import junit.framework.Assert.fail
import junit.framework.Assert.assertEquals

import org.junit.Before
import org.junit.Test
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

    @Test fun verify_provider_is_registered_correctly() {
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
}