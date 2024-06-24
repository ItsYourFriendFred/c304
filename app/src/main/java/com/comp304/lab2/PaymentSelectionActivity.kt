package com.comp304.lab2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_selection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the address & price from the selected housing option
        val selectedAddress = intent.getStringExtra("selectedAddress")
        val selectedPrice = intent.getFloatExtra("selectedPrice", 0.0f)

        // Display text of the chosen housing option for the buyer's visual reference
        val selectedHousingTextView = findViewById<TextView>(R.id.selectedHousing)
        selectedHousingTextView.text =
            getString(R.string.selectedItemPayment, selectedAddress, selectedPrice)

        // Store selected payment method and navigate + pass it to the PaymentInputActivity
        val buttonConfirmPayment = findViewById<Button>(R.id.buttonConfirmPayment)
        buttonConfirmPayment.setOnClickListener {
            val selectedPaymentMethodId =
                findViewById<RadioGroup>(R.id.radioGroupPayment).checkedRadioButtonId  // Returns -1 if nothing chosen
            if (selectedPaymentMethodId != -1) {
                val selectedPaymentMethod = when (selectedPaymentMethodId) {
                    R.id.radioButtonCash -> "Cash"
                    R.id.radioButtonCreditCard -> "Credit Card"
                    R.id.radioButtonDebitCard -> "Debit Card"
                    else -> ""
                }
                if (selectedPaymentMethod != "") {
                    // Show confirmation dialog
                    val yesNoDialog = AlertDialog.Builder(this)
                    yesNoDialog.setTitle("Confirm Payment Option")
                        .setMessage("Confirm selection: $selectedPaymentMethod?")
                        .setPositiveButton("Yes") { _, _ ->
                            when (selectedPaymentMethod) {
                                "Cash" -> {
                                    // Show payment information dialog for Cash option
                                    val okDialog = AlertDialog.Builder(this)
                                    okDialog.setTitle("Payment Information")
                                        .setMessage("Please visit your local office to pay")
                                        .setPositiveButton("OK") { _, _ ->
                                            // Skip PaymentInputActivity and go to PaymentCompletedActivity since cash is in-person
                                            val intent =
                                                Intent(this, PaymentCompletedActivity::class.java)
                                            startActivity(intent)
                                        }
                                        .show()
                                }
                                "Credit Card", "Debit Card" -> {
                                    // Start PaymentInputActivity for other payment methods
                                    val intent =
                                        Intent(this, PaymentInputActivity::class.java).apply {
                                            putExtra("selectedPaymentMethod", selectedPaymentMethod)
                                            putExtra("selectedPrice", selectedPrice)
                                            putExtra("selectedAddress", selectedAddress)
                                        }
                                    startActivity(intent)
                                }
                            }
                        }
                        // Setting No option to allow cancelling confirmation and allow re-selection
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                } else {
                    Toast.makeText(this, "Please select a valid payment method", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            }
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
}