package com.example.niaganow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.niaganow.R

class TransactionHistory : Fragment() {

    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var buttonExportPDF: Button
    private lateinit var recyclerViewTransactions: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate your XML file named transactionhistory.xml
        val view = inflater.inflate(R.layout.transactionhistory, container, false)

        // Bind views using findViewById
        spinnerMonth = view.findViewById(R.id.spinnerMonth)
        spinnerYear = view.findViewById(R.id.spinnerYear)
        buttonExportPDF = view.findViewById(R.id.buttonExportPDF)
        recyclerViewTransactions = view.findViewById(R.id.recyclerViewTransactions)

        // You can now set up spinner adapters, listeners, and RecyclerView logic
        return view
    }
}
