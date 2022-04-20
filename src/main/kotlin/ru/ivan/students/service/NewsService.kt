package ru.ivan.students.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.Course
import ru.ivan.students.domian.Headline
import ru.ivan.students.repository.CourseRepository
import ru.ivan.students.repository.HeadlineRepository
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Service
class NewsService {

    @Autowired
    private lateinit var headlineRepository: HeadlineRepository;


    fun getCourses(repeatTimes: Int): Boolean {
        val treeNode: MutableList<String> = mutableListOf()

        val url =
            URL("https://newsapi.org/v2/top-headlines?country=ru&category=science&apiKey=08dc9c2a63f1489abdb785f445aff415")
        val http = url.openConnection() as HttpURLConnection
        http.requestMethod = "GET"
        http.doOutput = true
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")



        http.responseCode.toString() + http.responseMessage

        val sb = StringBuilder()
        val br = BufferedReader(
            InputStreamReader(http.getInputStream(), "utf-8")
        )
        var line: String? = null
        while (br.readLine().also { line = it } != null) {
            sb.append(
                """
            $line
            
            """.trimIndent()
            )
        }
        br.close()

        http.disconnect()

        val node = ObjectMapper().readValue(sb.toString(), ObjectNode::class.java)
        if (node.has("articles")) {
            ObjectMapper().readTree(node.get("articles").toString()).map(JsonNode::toString).forEach {
                treeNode.add(it)
            }
        }


        treeNode.forEach {
            println(it)
            val node = ObjectMapper().readValue(it, ObjectNode::class.java)
            var headline = Headline(
                name = node.get("title").toString(),
                text = node.get("description").toString(),
                tag = "наука",
                source = node.get("url").toString()
            )

            headlineRepository.save(headline)
        }

        return true
    }
}

