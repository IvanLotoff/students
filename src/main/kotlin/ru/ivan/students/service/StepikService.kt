package ru.ivan.students.service

import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class StepikService {
    val url = "https://stepik.org/users/36369913"
    var user = "eFn4HbMGvQDwHQszbGDIIIT6I5pxSfWWyjqoR3Qw"
    var secret = "OtuEL5Z0PPmMCWhmVDA9eDIFebWhrIhFeDcRF1BNXvPS7WXH3vLXiYf13apqIIUDWN1Wifr0OpV6nf0fhKzAtBNkyJXMGkHlV17w69Bdffh3rRjoDq6Neq61iYOcOLkh"

    @Throws(IOException::class)
    fun getAccessToken() {
        // Create a new HTTP client
        val client = OkHttpClient().newBuilder().build()

        // Create the request body
        val mediaType = MediaType.parse("text/plain")
        val body = RequestBody.create(
            mediaType,
            "response_type=token&client_id=ClientId&username=user&password=userpassword&scope=process&grant_type=password"
        )

        // Build the request object, with method, headers
        val request: Request =Request.Builder()
            .url("https://stepik.org/oauth2/token/")
            .method("POST", body)
            .addHeader("Authorization", createAuthHeaderString(user, secret)!!)
            .build()
        // Perform the request, this potentially throws an IOException
        val response = client.newCall(request).execute()
        println(response)
    }

    // Just a helper metod to create the basic auth header
    private fun createAuthHeaderString(username: String, password: String): String? {
        val auth = "$username:$password"
        return "Basic $auth"
    }

    fun sendGet(): String {

//        var doc: Document? = null
//        try {
//            doc = Jsoup.connect(url).get()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val title = doc!!.title()
//        println(doc.text().split("—").first())


        val httpClient: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        httpClient.requestMethod = "GET"
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0")
        var res: String
        BufferedReader(InputStreamReader(httpClient.getInputStream())).use { `in` ->
            val response = StringBuilder()
            var line: String?
            while (`in`.readLine().also { line = it } != null) response.append(line).append("\n")
            res = response.toString()
        }

        //a#member 66.ember-view.link-secondndary. link_no-static-line
        var html: Document = Jsoup.parse(res)


        return html.text()
    }

//    fun sendGet(): String {
//        val html: Document = Jsoup.connect(url).get()
//        var res1= html.select("#ember56").first();
//        return ""
//    }
}

