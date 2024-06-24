package com.comp304.lab2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ApartmentActivity : AppCompatActivity() {

    private val apartmentModels: ArrayList<HousingModel> = ArrayList()
    private val apartmentImages: IntArray = intArrayOf(R.drawable.apartment1, R.drawable.apartment2, R.drawable.apartment3 )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_apartment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView: RecyclerView = findViewById(R.id.apartmentRecyclerView)

        setUpApartmentModels()

        val adapter = HousingRecyclerViewAdapter(apartmentModels)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
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
        val addresses = resources.getStringArray(R.array.apartment_addresses)
        val prices = resources.getStringArray(R.array.apartment_prices)

        for (i in addresses.indices) {
            val address = addresses[i]
            val price = prices[i].toFloatOrNull() ?: 0.0f
            val image = apartmentImages[i]
            val model = HousingModel(isChecked = false, address = address, price = price, imageResourceID = image)
            apartmentModels.add(model)
        }
    }
}