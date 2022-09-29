package com.tcall.tcall_test.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


internal class StringOperationsTest {

    @Test
    fun getTenthCharacter() {
        val input = "0123456789"
        val result = StringOperations.getTenthCharacter(input)
        assertThat(result, `is`(StringOperations.preText10thChar + '9'))
    }

    @Test
    fun getEveryTenthCharacter() {
        val input = "0123456789_testthe10Char With input"
        val result = StringOperations.getEveryTenthCharacter(input)


        val chars: Array<Char> = "90 ".toCharArray().toTypedArray()
        assertThat(result, `is`(StringOperations.preTextEvery10thChar + chars.contentToString()))
    }

    @Test
    fun getDistinctWordCount() {
        val input = "0123456789 test 23 te test 43 23"
        val result = StringOperations.getDistinctWordCount(input)
        assertThat(result, `is`(StringOperations.preTextDistinct + '5'))
    }
}