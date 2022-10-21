package com.example.wastebintracker.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.wastebintracker.R
import com.example.wastebintracker.databinding.FragmentDashboardBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.json.JSONObject
import kotlin.random.Random


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var dashboard: View

    // on below line we are creating
    // variables for our graph view
    lateinit var lineGraphView: GraphView
    lateinit var mainHandler: Handler

    var iter = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val refBtn = root.findViewById(com.example.wastebintracker.R.id.btnRefresh) as Button
//
//        refBtn.setOnClickListener(){
//
//        }

        Log.d("Debug","Before find view by ID")

        val queue = Volley.newRequestQueue(requireActivity())

        Log.d("Debug","Before update TextTask")

        Log.d("Debug","After update TextTask")



        // on below line we are initializing
        // our variable with their ids.
        lineGraphView = root.findViewById(com.example.wastebintracker.R.id.graphView)

        var seriesPoints = load_init_pts()

        val series = LineGraphSeries<DataPoint>(seriesPoints)
        // on below line we are adding
        // data series to our graph view.
        lineGraphView.addSeries(series)

        lineGraphView.getViewport().setYAxisBoundsManual(true);
        lineGraphView.getViewport().setXAxisBoundsManual(true);

        lineGraphView.getViewport().setMinX(0.0);
        lineGraphView.getViewport().setMaxX(7.0);
        lineGraphView.getViewport().setMinY(0.0);
        lineGraphView.getViewport().setMaxY(3.0);

        val gridLabel: GridLabelRenderer = lineGraphView.getGridLabelRenderer()
        gridLabel.setPadding(50)
        gridLabel.horizontalAxisTitle = "Previous Week"
        gridLabel.verticalAxisTitle = "Kilos"
        gridLabel.setNumHorizontalLabels(8);
        gridLabel.setNumVerticalLabels(4);

        val updateTextTask = object : Runnable {
            override fun run() {
                Log.d("Debug","beforefin getRequest")
                iter = getRequest(lineGraphView, queue, iter)
                Log.d("Debug","after getRequest")
                mainHandler.postDelayed(this, 5000)
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

fun DP(a: Double, b: Double): DataPoint {
    return DataPoint(a.toDouble(), b.toDouble())
}

fun load_init_pts(): Array<DataPoint>{

    var datapoints: Array<DataPoint> = Array(15){ DataPoint(0.0,0.0) }
    lateinit var pt: DataPoint

    // Load doubles into array of datapoint to plot
    datapoints[0] = DP(0.0, 0.0)

    for (i in 1..14){
        pt = DP(0.5*i, Random.nextDouble(0.0, 2.9))
        datapoints[i] = pt
    }

    return  datapoints

}

fun loadPtsSeries(readings: Array<Double>): Array<DataPoint>{

    var datapts: Array<DataPoint> = Array(15){ DataPoint(0.0,0.0) }
    lateinit var pt: DataPoint

    // Load doubles into array of datapoint to plot
    for (i in 0..14){
        pt = DP(0.5*i, readings[i])
        datapts[i] = pt
    }

    return  datapts
}

fun getRequest(lineGraph: GraphView, queue: RequestQueue, iter: Int): Int {

    Log.d("Debug","getRequest Start")
    //textView.text = "getRequest"
    val url =
        "https://api.thingspeak.com/channels/1899295/feeds.json?api_key=XBQ90E955L93FVBV&results=20"

    val stringRequest = StringRequest(

        Request.Method.GET, url,

        Response.Listener { responseString ->

            val jsonArray = JSONObject(responseString).getJSONArray("feeds")

            // Load data points into an array
            lateinit var feed: JSONObject
            var readingsArray: Array<Double> = Array(15){0.0}
            for (i in 0..14){
                feed = jsonArray.getJSONObject(i+iter)
                readingsArray.set(i,feed.getString("field1").toDouble())
            }

            // Load data into array of datpoints
            var datapoints = loadPtsSeries(readingsArray)
            var series = LineGraphSeries<DataPoint>(datapoints)

            // Plot data
            lineGraph.removeAllSeries()
            lineGraph.addSeries(series)

            Log.d("Out",responseString)
        },

        Response.ErrorListener { volleyError ->
            val errorMessage = volleyError.message
            Log.d("Error","Error Message returned by error listener")
        }
    )

    queue.add(stringRequest)

    return (iter+1)%5
}

//fun getRequest(lineGraph: GraphView, queue: RequestQueue, iter: Int): Int {
//
//    Log.d("Debug","getRequest Start")
//    //textView.text = "getRequest"
//    val url =
//        "https://api.thingspeak.com/channels/1899295/feeds.json?api_key=XBQ90E955L93FVBV&results=15"
//
//    val stringRequest = StringRequest(
//
//        Request.Method.GET, url,
//
//        Response.Listener { responseString ->
//
//            val jsonArray = JSONObject(responseString).getJSONArray("feeds")
//
//            // Load data points into an array
//            lateinit var feed: JSONObject
//            var readingsArray: Array<Double> = Array(15){0.0}
//            for (i in 0..14){
//                feed = jsonArray.getJSONObject(i)
//                readingsArray.set(i,feed.getString("field1").toDouble())
//            }
//
//            // Load data into array of datpoints
//            var datapoints = loadPtsSeries(readingsArray)
//            var series = LineGraphSeries<DataPoint>(datapoints)
//
//            // Plot data
//            lineGraph.removeAllSeries()
//            lineGraph.addSeries(series)
//
//            Log.d("Out",responseString)
//        },
//
//        Response.ErrorListener { volleyError ->
//            val errorMessage = volleyError.message
//            Log.d("Error","Error Message returned by error listener")
//        }
//    )
//
//    queue.add(stringRequest)
//
//    return (iter+1)%5
//}