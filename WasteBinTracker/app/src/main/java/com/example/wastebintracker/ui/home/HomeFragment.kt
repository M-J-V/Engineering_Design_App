package com.example.wastebintracker.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wastebintracker.R
import com.example.wastebintracker.databinding.FragmentHomeBinding
import com.example.wastebintracker.ui.dashboard.getRequest
import com.example.wastebintracker.ui.dashboard.loadPtsSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.json.JSONObject
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var home: View
    lateinit var mainHandler: Handler

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var autoDate = root.findViewById<TextView>(R.id.autoDate)
        var dateNow = Calendar.getInstance().time
        autoDate.setText(dateNow.toString().dropLast(23))

        var weightText = root.findViewById<TextView>(R.id.weightText)
        var foodText = root.findViewById<TextView>(R.id.foodText)
        val queue = Volley.newRequestQueue(requireActivity())

        val updateTextTask = object : Runnable {
            override fun run() {
                getRequestWeight(queue, weightText, foodText)
                mainHandler.postDelayed(this, 1000)
            }
        }

        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

fun getRequestWeight(queue: RequestQueue, weight: TextView, food:TextView) {

    Log.d("Debug","getRequest Start")
    //textView.text = "getRequest"
    val url =
        "https://api.thingspeak.com/channels/1899295/feeds.json?api_key=XBQ90E955L93FVBV&results=1"

    val stringRequest = StringRequest(

        Request.Method.GET, url,

        Response.Listener { responseString ->

            val jsonArray = JSONObject(responseString).getJSONArray("feeds")

            // Load data points into an array
            var feed = jsonArray.getJSONObject(0)
            val sensorReadingWeight = feed.getString("field1")
            val sensorReadingFood = feed.getString("field2")

            weight.text = "Current Trash Weight: " + sensorReadingWeight + "kg"

            if (sensorReadingFood == "1")
                food.text = "Trash is Rotting: Yes"
            else
                food.text = "Food Rotting: No"

            Log.d("Out",responseString)
        },

        Response.ErrorListener { volleyError ->
            val errorMessage = volleyError.message
            Log.d("Error","Error Message returned by error listener")
        }
    )

    queue.add(stringRequest)
}