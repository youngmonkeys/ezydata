package com.tvd12.kotlin.examples.jpa

import com.tvd12.ezyfox.tool.EzySQLTableCreator
import com.tvd12.ezyfox.tool.data.EzyCaseType
import com.tvd12.kotlin.examples.jpa.entity.Book

fun main() {
    val creator = EzySQLTableCreator(Book::class.java, EzyCaseType.CAMEL)
    println(creator.createScript())
}