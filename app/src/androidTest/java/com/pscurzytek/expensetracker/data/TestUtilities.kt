/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.pscurzytek.expensetracker.data

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import com.pscurzytek.expensetracker.Utils.PollingCheck


internal object TestUtilities {

    val testContentObserver: TestContentObserver
        get() = TestContentObserver.testContentObserver

    /**
     * Students: The test functions for insert and delete use TestContentObserver to test
     * the ContentObserver callbacks using the PollingCheck class from the Android Compatibility
     * Test Suite tests.
     * NOTE: This only tests that the onChange function is called; it DOES NOT test that the
     * correct Uri is returned.
     */
    internal class TestContentObserver private constructor(val mHT: HandlerThread) : ContentObserver(Handler(mHT.looper)) {
        var mContentChanged: Boolean = false

        /**
         * Called when a content change occurs.
         *
         *
         * To ensure correct operation on older versions of the framework that did not provide a
         * Uri argument, applications should also implement this method whenever they implement
         * the { #onChange(boolean, Uri)} overload.
         *
         * @param selfChange True if this is a self-change notification.
         */
        override fun onChange(selfChange: Boolean) {
            onChange(selfChange, null)
        }

        /**
         * Called when a content change occurs. Includes the changed content Uri when available.
         *
         * @param selfChange True if this is a self-change notification.
         * @param uri        The Uri of the changed content, or null if unknown.
         */
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            mContentChanged = true
        }

        /**
         * Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
         * It's useful to look at the Android CTS source for ideas on how to test your Android
         * applications. The reason that PollingCheck works is that, by default, the JUnit testing
         * framework is not running on the main Android application thread.
         */
        fun waitForNotificationOrFail() {
            object : PollingCheck(5000) {
                override fun check(): Boolean {
                    return mContentChanged
                }
            }.run()
            mHT.quit()
        }

        companion object {

            val testContentObserver: TestContentObserver
                get() {
                    val ht = HandlerThread("ContentObserverThread")
                    ht.start()
                    return TestContentObserver(ht)
                }
        }
    }
}
