package com.pscurzytek.expensetracker

import com.pscurzytek.expensetracker.data.extensions.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.util.*

/**
 * Created by p.s.curzytek on 1/27/2018.
 */
class ExampleUnitTest {

    @Test fun getDayAndMonth_dateStringInExpectedFormat_returnsDayAndMonth() {
        // given
        val dateString = "06-25-2015"

        // when
        val result = dateString.getDayAndMonth()

        // then
        assertEquals("06-25", result)
    }
    @Test fun getYear_dateStringInExpectedFormat_returnsYear() {
        // given
        val dateString = "06-25-2015"

        // when
        val result = dateString.getYear()

        // then
        assertEquals("2015", result)
    }
    @Test fun getDate_dateStringInExpectedFormat_returnsDate() {
        // given
        val dateString = "06-25-2015"
        // months are zero-based in GregorianCalendar
        val calendar = GregorianCalendar(2015, 5, 25)

        // when
        val result = dateString.getDate()

        // then
        val resultCalendar = GregorianCalendar()
        resultCalendar.time = result

        assertEquals(calendar.get(GregorianCalendar.YEAR), resultCalendar.get(GregorianCalendar.YEAR))
        assertEquals(calendar.get(GregorianCalendar.MONTH), resultCalendar.get(GregorianCalendar.MONTH))
        assertEquals(calendar.get(GregorianCalendar.DAY_OF_MONTH), resultCalendar.get(GregorianCalendar.DAY_OF_MONTH))
    }
    @Test fun getString_validLocalDate_returnsDateInExpectedFormat() {
        // given
        val localDate = LocalDate.of(2000, 5, 9)

        // when
        val result = localDate.getString()

        // then
        assertEquals("05-09-2000", result)
    }
}
