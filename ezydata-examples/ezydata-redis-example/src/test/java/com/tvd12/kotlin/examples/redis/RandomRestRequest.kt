package com.tvd12.kotlin.examples.redis

import com.tvd12.ezyfox.json.EzySimpleJsonWriter
import com.tvd12.ezyfox.tool.EzyObjectInstanceRandom
import com.tvd12.kotlin.examples.redis.request.AddBookRequest

fun main() {
    val random = EzyObjectInstanceRandom()
    val instance = random.randomObjectToMap(AddBookRequest::class.java, false)
    val jsonWriter = EzySimpleJsonWriter()
    println(jsonWriter.writeAsString(instance))
}