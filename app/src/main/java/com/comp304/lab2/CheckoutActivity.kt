package com.comp304.lab2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CheckoutActivity : AppCompatActivity() {

    private lateinit var housingModels: List<HousingModel>
    private var selectedItemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val apartmentAddresses = resources.getStringArray(R.array.apartment_addresses)
        val apartmentPrices = resources.getStringArray(R.array.apartment_prices).map { it.toFloat() }
        val condoAddresses = resources.getStringArray(R.array.condo_addresses)
        val condoPrices = resources.getStringArray(R.array.condo_prices).map { it.toFloat() }
        val detachedHomeAddresses = resources.getStringArray(R.array.detached_home_addresses)
        val detachedHomePrices = resources.getStringArray(R.array.detached_home_prices).map { it.toFloat() }
        val semiDetachedHomeAddresses = resources.getStringArray(R.array.semi_detached_home_addresses)
        val semiDetachedHomePrices = resources.getStringArray(R.array.semi_detached_home_prices).map { it.toFloat() }
        val townhouseAddresses = resources.getStringArray(R.array.townhouse_addresses)
        val townhousePrices = resources.getStringArray(R.array.townhouse_prices).map { it.toFloat() }

        // Concatenate the addresses and prices
        val addresses = apartmentAddresses + condoAddresses + detachedHomeAddresses + semiDetachedHomeAddresses + townhouseAddresses
        val prices = (apartmentPrices + condoPrices + detachedHomePrices + semiDetachedHomePrices + townhousePrices).toFloatArray()


        val imageResourceIds = intArrayOf(R.drawable.apartment1, R.drawable.apartment2, R.drawable.apartment3,
            R.drawable.condo1, R.drawable.condo2, R.drawable.condo3,
            R.drawable.detachedhome1, R.drawable.detachedhome2, R.drawable.detachedhome3,
            R.drawable.semidetachedhome1, R.drawable.semidetachedhome2, R.drawable.semidetachedhome3 ,
            R.drawable.townhouse1, R.drawable.townhouse2, R.drawable.townhouse3
        )

        // Create a housing model object for each housing that's been defined
        housingModels = addresses.indices.map { index ->
            HousingModel(
                isChecked = false,
                address = addresses[index],
                price = prices[index],
                imageResourceID = imageResourceIds[index]
            )
        }

        val radioGroup = findViewById<RadioGroup>(R.id.cartRadioGroup)

        // Get SharedPreferences instance & retrieve saved checked item IDs
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val checkedItemIds = sharedPreferences.all.keys.mapNotNull { it.toIntOrNull() }

        // Add RadioButtons dynamically to RadioGroup
        checkedItemIds.forEach { itemId ->
            // Gets the price and address for the checked items
            val address = getAddressForItemId(itemId)
            val price = getPriceForItemId(itemId)

            // Create a radio button, displaying the formatted text using the address & price + generate an ID for the view
            val radioButton = RadioButton(this)
            radioButton.text = "$address \nPrice: $${"%.2f".format(price)}"
            radioButton.id = View.generateViewId()

            // Style the margin between RadioButtons
            val layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.margin_top_bottom), 0, 0)
            radioButton.layoutParams = layoutParams

            // Add the radio button to the group
            radioGroup.addView(radioButton)

            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItemId = itemId // Update selected item ID
                }
            }
        }

        // Check if the radio group is empty and set the cartHintText to tell the customer to go back and make at least one selection
        val cartHintText: TextView = findViewById(R.id.cartHintText)
        if (radioGroup.childCount == 0) {
            cartHintText.text = getString(R.string.text_hint_no_selections)
        }

        // Store selected housing option and navigate + pass it to the PaymentSelectionActivity
        val buttonCheckout = findViewById<Button>(R.id.buttonCheckout)
        buttonCheckout.setOnClickListener {
            if (selectedItemId != -1) {
                val selectedAddress = getAddressForItemId(selectedItemId)
                val selectedPrice = getPriceForItemId(selectedItemId)

                // Pass selected item info & start activity
                val intent = Intent(this, PaymentSelectionActivity::class.java).apply {
                    putExtra("selectedAddress", selectedAddress)
                    putExtra("selectedPrice", selectedPrice)
                }
                startActivity(intent)
            } else {
                if (radioGroup.childCount == 0) {
                    Toast.makeText(this, getString(R.string.button_text_no_options_selected), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,
                        getString(R.string.text_no_radio_options_selected), Toast.LENGTH_LONG).show()
                }
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

    private fun getAddressForItemId(itemId: Int): String {
        return housingModels.firstOrNull { it.imageResourceID == itemId }?.address ?: ""
    }

    private fun getPriceForItemId(itemId: Int): Float {
        return housingModels.firstOrNull { it.imageResourceID == itemId }?.price ?: 0.0f
    }
}
