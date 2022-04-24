package ru.ivan.students.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.ivan.students.domian.Course
import ru.ivan.students.repository.CourseRepository
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Service
class StepikService {

    @Autowired
    private lateinit var courseRepository: CourseRepository;

    fun getAccessToken(): String {
        val url = URL("https://stepik.org/oauth2/token/")
        val http = url.openConnection() as HttpURLConnection
        http.requestMethod = "POST"
        http.doOutput = true
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        http.setRequestProperty(
            "Authorization",
            "Basic ZUZuNEhiTUd2UUR3SFFzemJHRElJSVQ2STVweFNmV1d5anFvUjNRdzpPdHVFTDVaMFBQbU1DV2htVkRBOWVESUZlYldockloRmVEY1JGMUJOWHZQUzdXWEgzdkxYaVlmMTNhcHFJSVVEV04xV2lmcjBPcFY2bmYwZmhLekF0Qk5reUpYTUdrSGxWMTd3NjlCZGZmaDNyUmpvRHE2TmVxNjFpWU9jT0xraA=="
        )

        val data = "grant_type=client_credentials"

        http.outputStream.write(data.toByteArray(Charsets.UTF_8))


        //println(http.responseCode.toString() + " " + http.responseMessage)

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

        System.out.println("" + sb.toString())

        val node = ObjectMapper().readValue(sb.toString(), ObjectNode::class.java)

        if (node.has("access_token")) {
            return node.get("access_token").toString().replace("\"", "")
        }

        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No token")
    }


    fun getCourses(repeatTimes: Int): Boolean {
        var treeNode: MutableList<String> = mutableListOf()
        var number = 0
        repeat(repeatTimes) {
            number++
            val url =
                URL("https://stepik.org/api/courses?page=$number&page_size=100&is_popular=true&is_paid=false&with_certificate=true")
            val http = url.openConnection() as HttpURLConnection
            http.requestMethod = "GET"
            http.doOutput = true
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            http.setRequestProperty(
                "Authorization",
                "Bearer " + getAccessToken()
            )


            http.responseCode.toString() + " " + http.responseMessage

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
            if (node.has("courses")) {
                ObjectMapper().readTree(node.get("courses").toString()).map(JsonNode::toString).forEach {
                    var lower = it.lowercase()
                    if (
                    //it.contains("sql") &&
                        !lower.contains("школ")
                        && !lower.contains("огэ")
                        && !lower.contains("егэ")
                        && !lower.contains("лицей")
                        && !lower.contains("олимпиада")
                    )
                        treeNode.add(it)
                }
            }
        }

        treeNode.forEach {
            println(it)
            val node = ObjectMapper().readValue(it, ObjectNode::class.java)
            var id = node.get("id").toString()
            var course: Course = Course(
                name = node.get("title").toString(),
                about = node.get("summary").toString(),
                source = "https://stepik.org/course/$id/promo"
            )
            courseRepository.save(course)
        }

        return true
    }
}

