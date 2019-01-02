package com.example.russellshepherd.weatherreport

import android.util.Log
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.net.ssl.HttpsURLConnection

/**
 * Author: JIGAR PATEL.
 * Tutorial_URL: https://galleonsoft.com/tutorial/
 */

object ApiGetPostHelper {

    // Send Parameters method
    fun SendParams(reqURL: String, postDataParams: HashMap<String, String>?): String {

        val weatherServer: URL
        var resultString = ""
        try {
            weatherServer = URL(reqURL)

            val conn = weatherServer.openConnection() as HttpURLConnection
            conn.readTimeout = 70000
            conn.connectTimeout = 70000
            conn.requestMethod = "POST"


            if (postDataParams != null) {
                // For Post encoded Parameters
                conn.doInput = true
                conn.doOutput = true
                val os = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(getPostDataString(postDataParams))
                writer.flush()
                writer.close()
                os.close()

            } else {
                conn.requestMethod = "GET"
            }


            val responseCode = conn.responseCode
            Log.i("responseCode: ", responseCode.toString() + "")
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                val allTextResponse = conn.inputStream.bufferedReader().use(BufferedReader::readText)
                resultString = allTextResponse

            } else {
                resultString = ""
            }
        } catch (e: Exception) {
            resultString = ""
            e.printStackTrace()
        }
        Log.i("results", resultString)
        return resultString

    }


    // Collect Params from HashMap and encode with url.
    @Throws(UnsupportedEncodingException::class)
    private fun getPostDataString(params: HashMap<String, String>): String {
        val result = StringBuilder()
        var first = true
        for ((key, value) in params) {
            if (first)
                first = false
            else
                result.append("&")

            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value, "UTF-8"))
        }

        return result.toString()
    }

}