package com.comp304.lab2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CondominiumActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val condoModels: ArrayList<HousingModel> = ArrayList()
    private val condoImages: IntArray = intArrayOf(R.drawable.condo1, R.drawable.condo2, R.drawable.condo3 )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_condominium)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val recyclerView: RecyclerView = findViewById(R.id.condoRecyclerView)

        setUpApartmentModels()

        val adapter = HousingRecyclerViewAdapter(condoModels, sharedPreferences)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val buttonCheckout: Button = findViewById(R.id.condoButton)
        buttonCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
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

    private fun setUpApartmentModels() {
        val addresses = resources.getStringArray(R.array.condo_addresses)
        val prices = resources.getStringArray(R.array.condo_prices)

        for (i in addresses.indices) {
            val address = addresses[i]
            val price = prices[i].toFloatOrNull() ?: 0.0f
            val image = condoImages[i]
            val model = HousingModel(isChecked = false, address = address, price = price, imageResourceID = image)
            condoModels.add(model)
        }
    }
}