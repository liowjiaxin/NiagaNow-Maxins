package com.example.niaganow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var langSpinner: Spinner
    private lateinit var rangeSelector: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dashboard, container, false)

        barChart = view.findViewById(R.id.peakBarChart)
        langSpinner = view.findViewById(R.id.langSpinner)
        rangeSelector = view.findViewById(R.id.rangeSelector)

        setupBarChart("daily") // default
        setupLanguageSpinner()
        setupRangeSelector()

        return view
    }

    private fun setupLanguageSpinner() {
        val languages = listOf("US English", "Mandarin", "Malay")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        langSpinner.adapter = adapter

        langSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = languages[position]
                Toast.makeText(requireContext(), "Selected: $selectedLanguage", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupRangeSelector() {
        rangeSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioDaily -> setupBarChart("daily")
                R.id.radioWeekly -> setupBarChart("weekly")
                R.id.radioMonthly -> setupBarChart("monthly")
            }
        }
    }

    private fun setupBarChart(range: String) {
        val (entries, labels) = when (range) {
            "weekly" -> Pair(
                listOf(
                    BarEntry(0f, 450f),
                    BarEntry(1f, 320f),
                    BarEntry(2f, 550f),
                    BarEntry(3f, 400f),
                    BarEntry(4f, 610f),
                    BarEntry(5f, 470f),
                    BarEntry(6f, 520f)
                ),
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            )
            "monthly" -> Pair(
                listOf(
                    BarEntry(0f, 800f),
                    BarEntry(1f, 620f),
                    BarEntry(2f, 700f),
                    BarEntry(3f, 920f)
                ),
                listOf("Week 1", "Week 2", "Week 3", "Week 4")
            )
            else -> Pair( // daily
                listOf(
                    BarEntry(0f, 120f),
                    BarEntry(1f, 150f),
                    BarEntry(2f, 80f),
                    BarEntry(3f, 200f),
                    BarEntry(4f, 170f)
                ),
                listOf("8am", "10am", "12pm", "2pm", "4pm")
            )
        }

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
                valueFormatter = IndexAxisValueFormatter(labels)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                labelCount = labels.size
                textSize = 12f
            }

            legend.isEnabled = true
            invalidate()
        }
    }
}
