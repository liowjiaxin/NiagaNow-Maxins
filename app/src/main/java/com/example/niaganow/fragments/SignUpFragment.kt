package com.example.niaganow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.niaganow.databinding.FragmentSignUpBinding
import com.example.niaganow.viewmodel.LoginViewModel

class SignUpFragment: Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        setupGenderSpinner()

        binding.signUpButton.setOnClickListener {
            handleSignUp()
        }
        return binding.root
    }

    private fun setupGenderSpinner() {
        val genderOptions = arrayOf("Male", "Female", "Others")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mySpinner.adapter = adapter
    }

    private fun handleSignUp(){
        val name = binding.nameEditText.text.toString().trim()
        val gender = binding.mySpinner.selectedItem.toString()
        val email = binding.emailEditText.text.toString().trim()
        val business = binding.businessEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val IC = binding.IC.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
            business.isEmpty() || phone.isEmpty() || IC.isEmpty()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if (!viewModel.validateSignUp(email, password)) {
            Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context, "Account Created for $name", Toast.LENGTH_SHORT).show()

        binding.nameEditText.text?.clear()
        binding.emailEditText.text?.clear()
        binding.businessEditText.text?.clear()
        binding.phoneEditText.text?.clear()
        binding.ICEditText.text?.clear()
        binding.mySpinner.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}