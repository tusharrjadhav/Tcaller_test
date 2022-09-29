package com.tcall.tcall_test.util

import android.text.SpannableStringBuilder
import androidx.annotation.VisibleForTesting
import androidx.core.text.bold

object StringOperations {

    @VisibleForTesting
    const val preText10thChar = "10th character displayed on the screen is:"

    @VisibleForTesting
    const val preTextEvery10thChar = "Every 10th is: "

    @VisibleForTesting
    const val preTextDistinct = "Distinct word count: "

    fun getTenthCharacter(value: String?): String {
        var tenthChar = preText10thChar
        if (!value.isNullOrEmpty() && value.length >= 10) {
            tenthChar = tenthChar.plus(value[9])
        }
        return tenthChar
    }

    fun getEveryTenthCharacter(value: String?): String {
        val every10thCharList = mutableListOf<String>()
        if (!value.isNullOrEmpty()) {
            var i = 9
            while (i < value.length) {
                val tenthChar = value[i].toString()
                if (tenthChar.isNotEmpty()) {
                    every10thCharList.add(tenthChar)
                }
                i += 10
            }
        }
        val char = preTextEvery10thChar
        return char + every10thCharList.toString()
    }

    fun getDistinctWordCount(value: String?): String {
        val char = preTextDistinct
        value?.let {
            val splits = it.split("[\\s\\t,]".toRegex())
            return char + splits.distinct().size.toString()
        }
        return char + "0"
    }
}