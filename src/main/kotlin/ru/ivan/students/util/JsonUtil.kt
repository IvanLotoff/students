package ru.ivan.students.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode


fun Any.toJson(): String {
    val objectMapper = ObjectMapper()
    return objectMapper.writeValueAsString(this)
}

fun String.getJsonField(field: String): Any? {
    val parent: JsonNode = ObjectMapper().readTree(this)
    return parent.get(field).asText()
//    return ObjectMapper().readValue(this, ObjectNode::class.java)
//        .get(field)
//        .toString()
//        .replace("\"", "")
}

/**
 * получаем поле из одного элемента, который обёрнут в массив
 * [
 *  {
 *      "user": "user"
 *  }
 * ]
 */
fun String.getJsonFieldFromArrayWrapped(field: String): Any? {
    val parent: JsonNode = ObjectMapper().readTree(this)
    return parent.get(0).get(field)
}