package com.muhammad.phone_auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var spinner: Spinner? = null
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinnerCountries)
        editText = findViewById(R.id.inputMbl)

        spinner!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,CountryData.countryNames)

        btnCont.setOnClickListener {
            val code = CountryData.countryAreaCodes[spinner!!.selectedItemPosition]
            val number = editText!!.text.toString()

            if(number.isEmpty() || number.length<10){
                Toast.makeText(this,"Invalid Number", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
            }

            val phnum = "+$code$number"
            val intent = Intent(this,VerifyPhoneActivity::class.java)
            intent.putExtra("phone",phnum)
            startActivity(intent)


        }

    }
}
