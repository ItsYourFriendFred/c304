package com.comp304.lab2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentInputActivity : AppCompatActivity() {
    private lateinit var customerNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var cardNumberEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var favouriteColourEditText: EditText
    private lateinit var favouriteNumberEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_input)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the EditText fields and the submit Button
        customerNameEditText = findViewById(R.id.editTextFullName)
        emailEditText = findViewById(R.id.editTextTextEmailAddress)
        cardNumberEditText = findViewById(R.id.editTextPhone)
        phoneEditText = findViewById(R.id.editTextNumber)
        addressEditText = findViewById(R.id.editTextTextPostalAddress)
        favouriteColourEditText = findViewById(R.id.editTextFavouriteColour)
        favouriteNumberEditText = findViewById(R.id.editTextFavouriteNumber)
        val submitButton: Button = findViewById(R.id.buttonSubmit)

        // OnClickListener for the submit button
        submitButton.setOnClickListener {
            // Validate inputs, if false return
            if (!validateInputs()) {
                return@setOnClickListener
            }

            // Retrieve the input from each EditText field
            val customerName = customerNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val cardNumber = cardNumberEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val favouriteColour = favouriteColourEditText.text.toString().trim()
            val favouriteNumber = favouriteNumberEditText.text.toString().trim()

            // String for the customer's submitted information
            val selectedPaymentMethod = intent.getStringExtra("selectedPaymentMethod")
            val customerInfo = "Name: $customerName" +
                    "\nEmail: $email" +
                    "\n$selectedPaymentMethod Number: $cardNumber" +
                    "\nPhone Number: $phone" +
                    "\nAddress: $address" +
                    "\nFavourite Colour: $favouriteColour" +
                    "\nFavourite Number: $favouriteNumber"

            // Create a dialog to display submitted payment details
            val okDialog = AlertDialog.Builder(this)
            okDialog.setTitle("Payment Confirmation")
                .setMessage("$customerInfo\n\nPayment Successful!")
                .setPositiveButton("OK") { _, _ ->
                    // Clear shared preferences once done
                    sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()

                    // Navigate to the PaymentCompletedActivity
                    val intent = Intent(this, PaymentCompletedActivity::class.java)

                    // If the data was needed for further processing - outside of the scope of this assignment
                    /*
                    intent.putExtra("customerName", customerName)
                    intent.putExtra("email", email)
                    intent.putExtra("cardNumber", cardNumber)
                    intent.putExtra("phone", phone)
                    intent.putExtra("address", address)
                    intent.putExtra("favouriteColour", favouriteColour)
                    intent.putExtra("favouriteNumber", favouriteNumber)
                    */

                    startActivity(intent)
                }
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_types_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_apartment -> {
                val intent = Intent(this, ApartmentActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_detached_home -> {
                val intent = Intent(this, DetachedHomeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_semi_detached_home -> {
                val intent = Intent(this, SemiDetachedHomeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_condominium_apartment -> {
                val intent = Intent(this, CondominiumActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_townhouse -> {
                val intent = Intent(this, TownHouseActivity::class.java)
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateInputs(): Boolean {
        // Validate Full Name
        if (customerNameEditText.text.trim().isEmpty()) {
            customerNameEditText.error = getString(R.string.text_error_required_field)
            return false
        }

        // Validate Email
        if (emailEditText.text.trim().isEmpty()) {
            emailEditText.error = getString(R.string.text_error_required_field)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches()) {
            emailEditText.error = getString(R.string.text_error_email_address)
            return false
        }

        // Validate Card Number
        val cardNumberNoWhitespace = cardNumberEditText.text.replace("\\s".toRegex(), "")  // Remove whitespaces to account for two common input formats, w/ or w/o spaces between every 4 digits
        if (cardNumberNoWhitespace.isEmpty()) {
            cardNumberEditText.error = getString(R.string.text_error_required_field)
            return false
        } else if (cardNumberNoWhitespace.length != 16) {
            cardNumberEditText.error = getString(R.string.text_error_card_number)
            return false
        }

        // Validate Phone Number
        if (phoneEditText.text.trim().isEmpty()) {
            phoneEditText.error = getString(R.string.text_error_required_field)
            return false
        } else if (!Patterns.PHONE.matcher(phoneEditText.text).matches()) {
            phoneEditText.error = getString(R.string.text_error_phone_number)
            return false
        } else if (phoneEditText.text.trim().length != 10) {
            phoneEditText.error = getString(R.string.text_error_phone_number)
            return false
        }

        //  Validate Postal Address
        if (addressEditText.text.trim().isEmpty()) {
            addressEditText.error = getString(R.string.text_error_required_field)
            return false
        }

        // Validate Favourite Colour
        if (favouriteColourEditText.text.trim().isEmpty()) {
            favouriteColourEditText.error = getString(R.string.text_error_required_field)
            return false
        }

        // Validate Favourite Number
        val favouriteNumber = favouriteNumberEditText.text.toString().trim()
        if (favouriteNumber.isEmpty()) {
            favouriteNumberEditText.error = getString(R.string.text_error_required_field)
            return false
        } else if (!favouriteNumber.matches("-?\\d+(\\.\\d+)?".toRegex())) {
            favouriteNumberEditText.error = getString(R.string.text_error_favourite_number_format)
            return false
        }

        return true
    }
}