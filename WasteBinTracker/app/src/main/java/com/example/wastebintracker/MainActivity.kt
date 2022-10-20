package com.example.wastebintracker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wastebintracker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import com.android.volley.Response
import com.android.volley.Request

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mainHandler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_weight_data, R.id.navigation_goals
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        Log.d("Debug","Before find view by ID")
//
//        var tvWeight = findViewById<TextView>(R.id.tvWeight)
//
//        Log.d("Debug","Before update TextTask")
//
//        val updateTextTask = object : Runnable {
//            override fun run() {
//                Log.d("Debug","beforefin getRequest")
//                getRequest(tvWeight)
//                Log.d("Debug","after getRequest")
//                mainHandler.postDelayed(this, 5000)
//            }
//        }
//
//        Log.d("Debug","After update TextTask")
//
//        mainHandler = Handler(Looper.getMainLooper())
//        mainHandler.post(updateTextTask)
    }
}


//fun getRequest(textView: TextView) {
//
//    Log.d("Debug","getRequest Start")
//
//    val url =
//        "https://api.thingspeak.com/channels/{your-channel-id}/fields/1.json?api_key={your-api-key}&results=2"
//
//    val stringRequest = StringRequest(
//
//        Request.Method.GET, url,
//
//        Response.Listener { responseString ->
//
//            val jsonArray = JSONObject(responseString).getJSONArray("feeds")
//            val feeds = jsonArray.getJSONObject(0)
//            val sensorReading = feeds.getString("field1")
//
//
//            textView.text = sensorReading
//        },
//
//        Response.ErrorListener { volleyError ->
//            val errorMessage = volleyError.message
//
//            textView.text = errorMessage
//        }
//    )
//}