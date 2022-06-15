package com.example.carpool_252867

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.carpool_252867.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myCalendar = Calendar.getInstance()
        var formatDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd MMMM YYYY", Locale.UK)
        } else {
            TODO("VERSION.SDK_INT < N")
        }

        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        var month = myCalendar.get(Calendar.MONTH)
        val year = myCalendar.get(Calendar.YEAR)
        val hour = myCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = myCalendar.get(Calendar.MINUTE)
        var date = formatDate.format(myCalendar.time)

        binding.buttonDate.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                date = formatDate.format(myCalendar.time)
                Toast.makeText(applicationContext, "Selected date: $date", Toast.LENGTH_SHORT).show()
            }
            DatePickerDialog(this, datePicker, year, month, day).show()
        }

        binding.buttonTime.setOnClickListener {

           TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{view, hour, minute -> binding.textViewResultMessage.setText("$hour:$minute")}, hour, minute, true ).show()
        }

        binding.buttonOffer.setOnClickListener {
            var message = ""
            if (binding.editTextTextStartPoint.text.isNotEmpty()){
                message += "Start point: " + binding.editTextTextStartPoint.text + "\n"
            }
            if (binding.editTextTextDestinationPoint.text.isNotEmpty()){
                message += "Destination point: " + binding.editTextTextDestinationPoint.text + "\n"
            }
            if (date.isNotEmpty()){
                message += "Date: $date \n"
            }

                message += "Time: $hour:$minute \n"

            if (binding.editTextTextPassengersNumber.text.isNotEmpty()){
                message += "Passengers number: " + binding.editTextTextPassengersNumber.text + "\n"
            }
            if (binding.editTextTextPricePerPassenger.text.isNotEmpty()){
                message += "Price per passenger: " + binding.editTextTextPricePerPassenger.text + "\n"
            }
            binding.textViewResultMessage.text = message;
        }















    }
}