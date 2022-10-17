package com.example.wastebintracker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wastebintracker.databinding.FragmentDashboardBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var dashboard: View

    // on below line we are creating
    // variables for our graph view
    lateinit var lineGraphView: GraphView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val refBtn = root.findViewById(com.example.wastebintracker.R.id.btnRefresh) as Button

        refBtn.setOnClickListener(){
            val new_points = arrayOf(
                DP(0, 16),
                DP(5, 25),
                DP(10, 27),
                DP(15, 0),
                DP(20, 4),
                DP(25, 13),
                DP(30, 19),
                DP(35, 21),
                DP(40, 0),
                DP(45, 10),
                DP(50, 12),
                DP(55, 18),
                DP(60, 23),
                DP(65, 0),
                DP(70, 5),

                )
            val new_series = LineGraphSeries<DataPoint>(new_points)

            lineGraphView.removeAllSeries()
            lineGraphView.addSeries(new_series)
        }


        // on below line we are initializing
        // our variable with their ids.
        lineGraphView = root.findViewById(com.example.wastebintracker.R.id.graphView)

        var series_points = load_init_pts()

        val series = LineGraphSeries<DataPoint>(series_points)
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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun DP(a: Int, b: Int): DataPoint {
        return DataPoint(a.toDouble()/10, b.toDouble()/10)
    }

    fun load_init_pts(): Array<DataPoint> {
        val points = arrayOf(
            DP(0, 0),
            DP(5, 8),
            DP(10, 14),
            DP(15, 16),
            DP(20, 25),
            DP(25, 27),
            DP(30, 0),
            DP(35, 4),
            DP(40, 13),
            DP(45, 19),
            DP(50, 21),
            DP(55, 0),
            DP(60, 10),
            DP(65, 12),
            DP(70, 18)
        )

        return points
    }
}