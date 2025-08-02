package com.example.niaganow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.niaganow.databinding.DashboardBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class Dashboard : AppCompatActivity() {

    private lateinit var binding: DashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ View Binding setup
        binding = DashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBarChart()
    }

    private fun setupBarChart() {
        val barChart: BarChart = binding.peakBarChart

        // ✅ Step 1: Create entries
        val entries = listOf(
            BarEntry(0f, 120f),  // 8am
            BarEntry(1f, 150f),  // 10am
            BarEntry(2f, 80f),   // 12pm
            BarEntry(3f, 200f),  // 2pm
            BarEntry(4f, 170f),  // 4pm
        )

        // ✅ Step 2: Create DataSet with color
        val dataSet = BarDataSet(entries, "Transactions")
        dataSet.color = ContextCompat.getColor(this, R.color.teal_700)

        // ✅ Step 3: Prepare BarData
        val barData = BarData(dataSet)
        barData.barWidth = 0.9f
        barData.setValueTextSize(12f)

        // ✅ Step 4: Configure BarChart
        barChart.apply {
            data = barData
            setFitBars(true)
            description.isEnabled = false
            setScaleEnabled(false)
            animateY(1000)

            axisRight.isEnabled = false
            axisLeft.axisMinimum = 0f

            // X-Axis settings
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(listOf("8am", "10am", "12pm", "2pm", "4pm"))
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
