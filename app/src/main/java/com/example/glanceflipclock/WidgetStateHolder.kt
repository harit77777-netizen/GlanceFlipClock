package com.example.glanceflipclock

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object WidgetStateHolder {
    data class WidgetState(
        val oldHours: String = "00",
        val newHours: String = "00",
        val oldMinutes: String = "00",
        val newMinutes: String = "00",
        val date: String = ""
    )

    fun getState(context: Context): WidgetState {
        val calendar = Calendar.getInstance()
        val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
        val minuteFormat = SimpleDateFormat("mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale("ru"))

        // Текущее время
        val newH = hourFormat.format(calendar.time)
        val newM = minuteFormat.format(calendar.time)
        val currentDate = dateFormat.format(calendar.time)

        // Агрессивный трюк: отматываем время на 1 минуту назад
        calendar.add(Calendar.MINUTE, -1)
        val oldH = hourFormat.format(calendar.time)
        val oldM = minuteFormat.format(calendar.time)

        return WidgetState(
            oldHours = oldH,
            newHours = newH,
            oldMinutes = oldM,
            newMinutes = newM,
            date = currentDate
        )
    }
}
