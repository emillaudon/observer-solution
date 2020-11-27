package com.example.observerapp

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    lateinit var button : Button

    lateinit var text : TextView

    var quoteTextViewText:String by Delegates.observable<String>("Yes") { property, oldValue, newValue ->
            println("loggg")
            println(newValue)
            runOnUiThread {
            text.text = newValue
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        button = findViewById<Button>(R.id.button_first5)
        text = findViewById<TextView>(R.id.textview_first5)

        button.setOnClickListener { view ->
            getData()
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "No", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getData() {
        val url = URL("https://quote-garden.herokuapp.com/api/v2/quotes/random")

        val thread = Thread(Runnable {
            try {
                with(url.openConnection() as HttpsURLConnection) {
                    requestMethod = "GET"

                    inputStream.bufferedReader().use {
                        it.lines().forEach { line ->
                            val jsonObject = JSONObject(line)

                            if (jsonObject["statusCode"] == 200) {
                                val quote = jsonObject["quote"] as JSONObject
                                val quoteText = quote["quoteText"] as String

                                quoteTextViewText = quoteText

                            } else {
                                quoteTextViewText = "No data from server, try again, status code: " + jsonObject["statusCode"]
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                quoteTextViewText = "Unable to make connection, try again"
            }
        })

        thread.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDataButtonTwo() {
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
