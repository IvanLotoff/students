package ru.ivan.students.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class StepikService {
    val url = "https://stepik.org/cert/990532"

//    fun sendGet(): String {
//        val httpClient: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
//        httpClient.requestMethod = "GET"
//        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0")
//        var res: String
//        BufferedReader(InputStreamReader(httpClient.getInputStream())).use { `in` ->
//            val response = StringBuilder()
//            var line: String?
//            while (`in`.readLine().also { line = it } != null) response.append(line).append("\n")
//            res = response.toString()
//        }
//        //a#member 66.ember-view.link-secondndary. link_no-static-line
//        var html: Document = Jsoup.parse(res)
//        var res1= html.select("#ember56").first();
//        return res
//    }

    fun sendGet(): String {
        val html: Document = Jsoup.connect(url).get()
        var res1= html.select("#ember56").first();
        return ""
    }
}

