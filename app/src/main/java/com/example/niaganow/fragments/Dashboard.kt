package com.example.niaganow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.niaganow.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class Dashboard : Fragment() {

    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dashboard, container, false)

        // ✅ Find BarChart manually
        barChart = view.findViewById(R.id.peakBarChart)

        setupBarChart()

        return view
    }

    private fun setupBarChart() {
        val entries = listOf(
            BarEntry(0f, 120f),
            BarEntry(1f, 150f),
            BarEntry(2f, 80f),
            BarEntry(3f, 200f),
            BarEntry(4f, 170f)
        )

        val dataSet = BarDataSet(entries, "Transactions")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.teal_700)

        val barData = BarData(dataSet)
        barData.barWidth = 0.9f
        barData.setValueTextSize(12f)

        barChart.apply {
            data = barData
            setFitBars(true)
            description.isEnabled = false
            setScaleEnabled(false)
            animateY(1000)

            axisRight.isEnabled = false
            axisLeft.axisMinimum = 0f

            xAxis.apply {
                valueFormatter =
                    IndexAxisValueFormatter(listOf("8am", "10am", "12pm", "2pm", "4pm"))
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                labelCount = 5
                textSize = 12f
            }

            legend.isEnabled = true
            invalidate()
        }
    }
}