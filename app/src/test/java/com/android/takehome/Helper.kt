package com.android.takehome

import java.io.BufferedReader
import java.io.InputStreamReader
object Helper {
    fun readResourceFile(filename: String): String{
        val inputStream= Helper::class.java.getResourceAsStream(filename)
        val builder=StringBuilder()
        val reader= BufferedReader(InputStreamReader(inputStream))

        reader.readLines().forEach{
            builder.append(it)
        }
        return builder.toString()
    }
}