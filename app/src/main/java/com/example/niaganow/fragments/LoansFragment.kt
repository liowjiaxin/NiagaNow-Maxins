package com.example.niaganow.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.niaganow.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class LoansFragment : Fragment() {

    private lateinit var loanAmountInput: EditText
    private lateinit var tenureInput: EditText
    private lateinit var resultText: TextView
    private lateinit var predictButton: Button

    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.loans, container, false)

        // Initialise UI components
        loanAmountInput = view.findViewById(R.id.loan_amount_input)
        tenureInput = view.findViewById(R.id.tenure_input)
        resultText = view.findViewById(R.id.result_text)
        predictButton = view.findViewById(R.id.predict_button)

        predictButton.setOnClickListener {
            val loanAmountStr = loanAmountInput.text.toString()
            val tenureStr = tenureInput.text.toString()

            if (loanAmountStr.isEmpty() || tenureStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loanAmount = loanAmountStr.toInt()
            val tenure = tenureStr.toDouble()

            sendLoanDataToApi(loanAmount, tenure)
        }

        return view
    }

    private fun sendLoanDataToApi(loanAmount: Int, tenureYears: Double) {
        val url = "http://10.100.240.9:5000/predict_provider"



        val json = JSONObject().apply {
            put("loan_amount", loanAmount)
            put("tenure_years", tenureYears)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    resultText.text = "API call failed: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val provider = jsonResponse.getString("provider")
                        activity?.runOnUiThread {
                            resultText.text = "Recommended Provider: $provider"
                        }
                    } catch (e: Exception) {
                        activity?.runOnUiThread {
                            resultText.text = "Unexpected response"
                        }
                    }
                }
            }
        })
    }
}
