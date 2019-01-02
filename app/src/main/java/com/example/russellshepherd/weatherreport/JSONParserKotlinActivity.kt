package com.example.russellshepherd.weatherreport


import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.SimpleAdapter
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class JSONParserKotlinActivity : AppCompatActivity() {

    lateinit var HYXC_weather: ListView
    lateinit var Pbar: ProgressBar
    internal var weatherList = ArrayList<HashMap<String, String>>()
    internal var getLatestWeatherAPI: String = ""

    // Checking Internet is available or not
    private val isNetworkConnected: Boolean
        get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null


    // Show BackButton on Actionbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jsonparser_kotlin)

        val START_DATE = intent.getStringExtra("START_DATE")
        val END_DATE = intent.getStringExtra("END_DATE")

        getLatestWeatherAPI = "https://1a46f20b.ngrok.io/weather/range?start_date=${START_DATE}&end_date=${END_DATE}"


        // Show BackButton and Set custom Title on Actionbar
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "花園新城WeatherReport"
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }

        // findViewById and set view id
        Pbar = findViewById(R.id.Pbar)
        HYXC_weather = findViewById(R.id.Lv_weather)

        if (isNetworkConnected) {
            // Call AsyncTask for getting developer list from server (JSON Api)
            getWeather().execute()
        } else {
            Toast.makeText(applicationContext, "No Internet Connection Yet!", Toast.LENGTH_SHORT).show()
        }

    }


    @SuppressLint("StaticFieldLeak")
    inner class getWeather : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            // Show Progressbar for batter UI
            Pbar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg voids: Void): String {

            // Only Get JSON api send HashMap null see below comment example
            // return ApiGetPostHelper.SendParams(getLatestWeatherAPI, null);
            // Send the HttpPostRequest by HttpURLConnection and receive a Results in return string
            return ApiGetPostHelper.SendParams(getLatestWeatherAPI, null)
        }

        override fun onPostExecute(results: String?) {
            // Hide Progressbar
            Pbar.visibility = View.GONE

            if (results != null) {
                // See Response in Logcat for understand JSON Results and make DeveloperList
                Log.i("onPostExecute: ", results)
            }

            try {
                // Create JSONObject from string response if your response start from Array [ then create JSONArray
                val rootJsonObject = JSONObject(results)

                if (rootJsonObject.length() > 0) {
                    val weatherArray = rootJsonObject.getString("data")

                    val mJsonArray = JSONArray(weatherArray)

                    for (i in 0 until mJsonArray.length()) {
                        // Get single JSON object node
                        val sObject = mJsonArray.get(i).toString()
                        val mItemObject = JSONObject(sObject)
                        // Get String value from json object
                        val datetime = mItemObject.getString("datetime")
                        val temperature = mItemObject.getDouble("temperature")
                        val humidity = mItemObject.getDouble("humidity")

                        // hash map for single jsonObject you can create model.
                        val mHash = HashMap<String, String>()
                        // adding each child node to HashMap key => value/data
                        // Now I'm adding some extra text in value
                        mHash["Timestamp"] = "Timestamp: $datetime"
                        mHash["Temperature"] = "Temperature: $temperature"
                        mHash["Humidity"] = "Humidity: $humidity"
                        // Adding HashMap pair list into developer list
                        weatherList.add(mHash)
                    }

                    // This is simple Adapter (android widget) for ListView
                    val simpleAdapter = SimpleAdapter(
                        applicationContext, weatherList,
                        R.layout.simple_listview_item,
                        // Add String[] name same as HashMap Key
                        arrayOf("Timestamp", "Temperature", "Humidity"),
                        intArrayOf(R.id.w_timestamp, R.id.w_temperature, R.id.w_humidity))

                    HYXC_weather.adapter = simpleAdapter

                    HYXC_weather.setOnItemClickListener { parent, view, position, id ->
                        Toast.makeText(applicationContext, "Selected item is " + position, Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(applicationContext, "No Weather Reports Found!", Toast.LENGTH_SHORT).show()
                }


            } catch (e: JSONException) {
                Toast.makeText(applicationContext, "Something wrong. Try Again!", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        }
    }


}