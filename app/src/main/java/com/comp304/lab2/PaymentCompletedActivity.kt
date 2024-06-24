package com.comp304.lab2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentCompletedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_completed)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set a thank you message upon payment completion
        val paymentCompleteMessageString = getString(R.string.text_payment_complete, getString(R.string.app_name))
        val paymentCompleteTextView: TextView = findViewById(R.id.textPaymentComplete)
        paymentCompleteTextView.text = paymentCompleteMessageString

        val buttonReturn: Button = findViewById(R.id.buttonReturn)
        buttonReturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    // Hide the options menu, but still allow the back arrow to function
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Returning false will not inflate the menu, thus hiding it
        return false
    }
    // Hide the options menu, but still allow the back arrow to function
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}