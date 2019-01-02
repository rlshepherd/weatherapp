package com.example.russellshepherd.weatherreport

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.View
import android.widget.Button
import java.util.*
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.DatePicker
import android.widget.Toast
import java.text.SimpleDateFormat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var textview_date1: TextView? = null
    var textview_date2: TextView? = null
    var sendButton: Button? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the references from layout file
        textview_date1 = this.text_view_date_1
        textview_date2 = this.text_view_date_2

        textview_date1!!.text = "----/--/--"
        textview_date2!!.text = "----/--/--"

        sendButton = this.get_weather

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        textview_date1!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@MainActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        val dateSetListener2 = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView2()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        textview_date2!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@MainActivity,
                    dateSetListener2,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        fun getTheWeather() {
            if (textview_date1!!.text != null && textview_date2!!.text != null) {
                startActivity(Intent(this, JSONParserKotlinActivity::class.java).apply {
                    putExtra("START_DATE", textview_date1!!.text.toString())
                    putExtra("END_DATE", textview_date2!!.text.toString())
                })
            } else {
                Toast.makeText(applicationContext, "Please Provide Both Dates!", Toast.LENGTH_SHORT).show()
            }

        }

        sendButton!!.setOnClickListener{ getTheWeather() }
    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date1!!.text = sdf.format(cal.getTime())
    }

    private fun updateDateInView2() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date2!!.text = sdf.format(cal.getTime())
    }

}
