package com.salya.savorcapstone.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.salya.savorcapstone.InputManualActivity
import com.salya.savorcapstone.R

class InputOptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_option)

        val buttonPhoto = findViewById<Button>(R.id.button2)
        val buttonManual = findViewById<Button>(R.id.button3)

        buttonPhoto.setOnClickListener {
            val intent = Intent(this, InputFromGalleryActivity::class.java)
            startActivity(intent)
        }

        buttonManual.setOnClickListener {
            val intent = Intent(this, InputManualActivity::class.java)
            startActivity(intent)
        }
    }
}