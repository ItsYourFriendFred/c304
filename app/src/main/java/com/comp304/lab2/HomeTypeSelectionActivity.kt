package com.comp304.lab2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeTypeSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_type_selection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the non-menu navigation buttons
        val buttonApartmentType: Button = findViewById(R.id.apartmentTypeButton)
        val buttonCondoType: Button = findViewById(R.id.condoTypeButton)
        val buttonDetachedHomeType: Button = findViewById(R.id.detachedHomeTypeButton)
        val buttonSemiDetachedHomeType: Button = findViewById(R.id.semiDetachedHomeTypeButton)
        val buttonTownhouseType: Button = findViewById(R.id.townhouseTypeButton)

        // Navigate to the corresponding activity
        buttonApartmentType.setOnClickListener {
            val intent = Intent(this, ApartmentActivity::class.java)
            startActivity(intent)
        }
        buttonCondoType.setOnClickListener {
            val intent = Intent(this, CondominiumActivity::class.java)
            startActivity(intent)
        }
        buttonDetachedHomeType.setOnClickListener {
            val intent = Intent(this, DetachedHomeActivity::class.java)
            startActivity(intent)
        }
        buttonSemiDetachedHomeType.setOnClickListener {
            val intent = Intent(this, SemiDetachedHomeActivity::class.java)
            startActivity(intent)
        }
        buttonTownhouseType.setOnClickListener {
            val intent = Intent(this, TownHouseActivity::class.java)
            startActivity(intent)
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