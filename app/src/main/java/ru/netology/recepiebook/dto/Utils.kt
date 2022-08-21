package ru.netology.recepiebook.dto

import android.annotation.SuppressLint
import android.os.Bundle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.floor
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Utils {
    companion object {
        fun counter(count: Int) : String {
            return when (count) {
                in 0..999 -> count.toString()
                in 1000..1099 -> String.format("%d", count / 1000) + "K"
                in 1100..9999 -> String.format("%.1f", (floor(count / 100.toDouble())) / 10) + "K"
                in 10_000..999_999 -> String.format("%d", count / 1000) + "K"
                in 1_000_000..999_999_999 ->
                    String.format("%.1f", (floor(count / 100_000.toDouble())) / 10) + "M"
                else -> "> 1 Bn"
            }
        }

        @SuppressLint("NewApi")
        fun addLocalDataTime(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy в HH:mm")
            return current.format(formatter).toString()
        }
    }

    object StringArg : ReadWriteProperty<Bundle, String?> {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? {
            return thisRef.getString(property.name)
        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }
    }

    object LongArg : ReadWriteProperty<Bundle, Long> {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): Long {
            return thisRef.getLong(property.name)
        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Long) {
            thisRef.putLong(property.name, value)
        }
    }

}