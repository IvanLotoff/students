package ru.ivan.students.service

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class StepikService {

    fun getAccessToken() {
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


        println(http.responseCode.toString() + " " + http.responseMessage)

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
        System.out.println("" + sb.toString())

        http.disconnect()


    }

//    fun sendGet(): String {
//
////        var doc: Document? = null
////        try {
////            doc = Jsoup.connect(url).get()
////        } catch (e: IOException) {
////            e.printStackTrace()
////        }
////        val title = doc!!.title()
////        println(doc.text().split("—").first())
//
//
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
//
//        //a#member 66.ember-view.link-secondndary. link_no-static-line
//        var html: Document = Jsoup.parse(res)
//
//
//        return html.text()
//    }
//
////    fun sendGet(): String {
////        val html: Document = Jsoup.connect(url).get()
////        var res1= html.select("#ember56").first();
////        return ""
////    }
}

