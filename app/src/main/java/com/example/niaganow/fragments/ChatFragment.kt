package com.example.niaganow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.niaganow.databinding.FragmentChatBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class ChatFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Send button action
        binding.sendButton.setOnClickListener {
            val inputText = binding.messageInput.text.toString().trim()
            if (inputText.isNotBlank()) {
                binding.chatOutput.append("👤 You: $inputText\n")
                binding.messageInput.text?.clear()
                sendMessageToBot(inputText)
            }
        }

        // Close button action
        binding.closeButton.setOnClickListener {
            dismiss()  // Closes the bottom sheet dialog
        }
    }

    private fun sendMessageToBot(userMessage: String) {
        val json = JSONObject().apply {
            put("message", userMessage)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://10.0.2.2:8000/chat") // Localhost for Android emulator
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    binding.chatOutput.append("❌ Bot error: ${e.localizedMessage}\n\n")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        requireActivity().runOnUiThread {
                            binding.chatOutput.append("❌ Server error: ${response.code}\n\n")
                        }
                    } else {
                        val res = JSONObject(response.body!!.string())
                        val reply = res.getString("response")
                        val lang = res.getString("language")
                        requireActivity().runOnUiThread {
                            binding.chatOutput.append("🤖 Bot ($lang): $reply\n\n")
                        }
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
