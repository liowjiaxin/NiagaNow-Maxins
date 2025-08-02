package com.example.niaganow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niaganow.R
import com.example.niaganow.adapter.TransactionAdapter
import com.example.niaganow.model.Transaction
import java.util.*

class TransactionHistory : Fragment() {

    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var buttonExportPDF: Button
    private lateinit var recyclerViewTransactions: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.transactionhistory, container, false)

        spinnerMonth = view.findViewById(R.id.spinnerMonth)
        spinnerYear = view.findViewById(R.id.spinnerYear)
        buttonExportPDF = view.findViewById(R.id.buttonExportPDF)
        recyclerViewTransactions = view.findViewById(R.id.recyclerViewTransactions)

        // --- Populate Month Spinner ---
        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = monthAdapter

        // --- Populate Year Spinner ---
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (2020..currentYear).map { it.toString() }.reversed()
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = yearAdapter

        // --- Preselect Current Month and Year ---
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) // 0-based: January = 0
        spinnerMonth.setSelection(currentMonth)

        val currentYearIndex = years.indexOf(currentYear.toString())
        if (currentYearIndex != -1) {
            spinnerYear.setSelection(currentYearIndex)
        }

        // --- Dummy Transactions ---
        val dummyTransactions = listOf(
            Transaction("TX001", "2025-08-01", "Payment to Vendor", "RM 10.00", "Completed"),
            Transaction("TX002", "2025-08-01", "Refund from Store", "RM 15.50", "Completed"),
            Transaction("TX003", "2025-07-30", "Online Purchase", "RM 25.00", "Pending"),
            Transaction("TX004", "2025-07-28", "Bill Payment", "RM 100.00", "Completed"),
            Transaction("TX005", "2025-07-27", "Subscription", "RM 8.99", "Failed")
        )

        recyclerViewTransactions.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTransactions.adapter = TransactionAdapter(dummyTransactions)

        return view
    }
}
