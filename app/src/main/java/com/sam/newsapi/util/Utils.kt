package com.sam.newsapi.util

import java.text.SimpleDateFormat
import java.util.*


object Utils {
    const val BASE_URL = "https://newsapi.org/"


    @JvmStatic
    fun getSourceAndTime(sourceName: String?, dateString: String?): String {
        val sb = StringBuilder("")
        if (sourceName != null) sb.append(sourceName)
        if (sb.toString() != "") sb.append(" • ")
        getRelativeDateString(dateString)?.let { sb.append(it) }
        return sb.toString()
    }


    @JvmStatic
    fun getByLine(sourceName: String?, author: String?): String? {
         return author ?: sourceName
    }

    @JvmStatic
    fun getLocalDateAndTime(dateString: String?): String? {
        val calendar: Calendar = Calendar.getInstance()
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val destFormat = SimpleDateFormat("dd-MM-yyyy hh:mm aaa", Locale.getDefault())
        sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
        destFormat.timeZone = calendar.timeZone
        val convertedDate: Date
        try {
            convertedDate = sourceFormat.parse(dateString)
            return destFormat.format(convertedDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getRelativeDateString(dateString: String?): String? {
        val localDateAndTime = getLocalDateAndTime(dateString)
        val dateFrom: Date
        dateFrom = try {
            SimpleDateFormat("dd-MM-yyyy hh:mm aaa", Locale.getDefault()).parse(localDateAndTime)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val calendar: Calendar = Calendar.getInstance()
        val dateTo: Date = calendar.time
        val diff: Long = dateTo.time - dateFrom.time
        var diffMinutes = diff / (60 * 1000)
        var diffHours = diff / (60 * 60 * 1000)
        val diffDays = diff / (24 * 60 * 60 * 1000)
        diffHours -= diffDays * 24
        diffMinutes -= diffHours * 60
        if (diffDays > 0) {
            return if (diffDays == 1L) "Yesterday" else "$diffDays Days ago"
        }
        if (diffHours > 0) {
            return if (diffHours == 1L) "An hour ago" else "$diffHours hours ago"
        }
        return if (diffMinutes > 0) {
            if (diffMinutes == 1L) "A minute ago" else "$diffMinutes minutes ago"
        } else "Just now"
    }

}