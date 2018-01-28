package com.pscurzytek.expensetracker.data.extensions

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by p.s.curzytek on 1/27/2018.
 */

private val dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.US)

fun String.getDayAndMonth(): String {
    val date = LocalDate.parse(this, dateFormat)
    val formatter = DateTimeFormatter.ofPattern("MM-dd")

    return date.format(formatter)
}

fun String.getYear(): String {
    val date = LocalDate.parse(this, dateFormat)
    val formatter = DateTimeFormatter.ofPattern("yyyy")

    return date.format(formatter)
}

fun String.getDate(): Date {
    val localDate = LocalDate.parse(this, dateFormat)
    val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    return date
}

fun LocalDate.getString(): String {
    return this.format(dateFormat)
}