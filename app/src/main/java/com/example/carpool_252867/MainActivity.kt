package com.example.carpool_252867

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.carpool_252867.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myCalendar = Calendar.getInstance()
        var formatDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd MMMM YYYY", Locale.GERMANY)
        } else {
            TODO("VERSION.SDK_INT < N")
        }

        //val currentDay = myCalendar.get(Calendar.DAY_OF_MONTH)
        //var currentMonth = myCalendar.get(Calendar.MONTH)
        //val currentYear = myCalendar.get(Calendar.YEAR)
        val currentHour = myCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = myCalendar.get(Calendar.MINUTE)
        var selectedDay = myCalendar.get(Calendar.DAY_OF_MONTH)
        var selectedMonth = myCalendar.get(Calendar.MONTH)
        var selectedYear = myCalendar.get(Calendar.YEAR)
        var selectedHour = myCalendar.get(Calendar.HOUR_OF_DAY)
        var selectedMinute = myCalendar.get(Calendar.MINUTE)
        var currentDateText = formatDate.format(myCalendar.time)
        var selectedDateText  = formatDate.format(myCalendar.time)//= formatDate.format(myCalendar.time)
        var selectedDate = myCalendar.time
        var currentDate = myCalendar.time

        binding.buttonDate.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, currentYear, currentMonth, currentDay ->
                //myCalendar.set(Calendar.YEAR, currentYear) // ustawianie domyślnej daty
                //myCalendar.set(Calendar.MONTH, currentMonth)
                //myCalendar.set(Calendar.DAY_OF_MONTH, currentDay)
                selectedYear = myCalendar.get(Calendar.YEAR)
                selectedMonth = myCalendar.get(Calendar.MONTH)
                selectedDay = myCalendar.get(Calendar.DAY_OF_MONTH)
                selectedDateText = formatDate.format(myCalendar.time)
                selectedDate = myCalendar.time

                Toast.makeText(applicationContext, "Selected date: $selectedDateText", Toast.LENGTH_SHORT).show()
            }
            val dpd = DatePickerDialog(this, datePicker, selectedYear, selectedMonth, selectedDay)
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000 // ustawianie minimalnej daty do wybrania
            dpd.show()
        }

        binding.buttonTime.setOnClickListener {
           TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{view, currentHour, currentMinute ->
               //myCalendar.set(Calendar.HOUR_OF_DAY, currentHour)
               //myCalendar.set(Calendar.MINUTE, currentMinute)
               //selectedHour = myCalendar.get(Calendar.HOUR)
               //selectedMinute = myCalendar.get(Calendar.MINUTE)
           }, selectedHour, selectedMinute, true ).show()
        }

        binding.buttonOffer.setOnClickListener {
            var message = ""
            if (binding.editTextTextStartPoint.text.isNotEmpty()){
                message += "Start point: " + binding.editTextTextStartPoint.text + "\n"
            }
            if (binding.editTextTextDestinationPoint.text.isNotEmpty()){
                message += "Destination point: " + binding.editTextTextDestinationPoint.text + "\n"
            }
            message += "DateText: $selectedDateText \n"
            message += "Date values : $selectedYear $selectedMonth $selectedDay \n"

            message += "Current Date: $currentDateText \n"
            message += "Selected date is after current day: " + selectedDate.compareTo(currentDate) + "\n"

            if(selectedDate.equals(currentDate) && (selectedHour < currentHour || (selectedHour == currentHour && selectedMinute < currentMinute))){
                // wybrano czas przed aktualną chwilą
                Toast.makeText(applicationContext, "Select later time, or different day!", Toast.LENGTH_LONG).show()
            }
            else{
                message += "Time: $selectedHour:$selectedMinute \n"
            }
            message += "Time: $selectedHour:$selectedMinute \n"

            message += "Current Time: $currentHour:$currentMinute \n"
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