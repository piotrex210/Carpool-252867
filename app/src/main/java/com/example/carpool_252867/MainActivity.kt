package com.example.carpool_252867

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.carpool_252867.databinding.ActivityMainBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var ridesArrayAdapter:ArrayAdapter<RideModel>
    @SuppressLint("NewApi")
    lateinit var dataBaseHelper: DataBaseHelper

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBaseHelper = DataBaseHelper(applicationContext)
        dataBaseHelper.deleteErrors()
        showRidesOnListViews()

        val myCalendar = Calendar.getInstance()
        var formatDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd MMMM YYYY", Locale.UK)
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
        var selectedTime = ""

        binding.buttonViewAll.setOnClickListener {
            val allRides = dataBaseHelper.getAll()
            dataBaseHelper.deleteErrors()
            showRidesOnListViews()
        }

        binding.buttonOffer.setOnClickListener {
            var rideModel:RideModel
            try {
                rideModel = RideModel(-1, binding.editTextTextStartPoint.text.toString(), binding.editTextTextDestinationPoint.text.toString(),
                selectedDateText, selectedTime, Integer.parseInt(binding.editTextTextPassengersNumber.text.toString()),
                    Integer.parseInt(binding.editTextTextPricePerPassenger.text.toString()))
            }
            catch (e: Exception){
                rideModel = RideModel(-1, "error", "error",
                    "error", "error", -1, -1)
                Toast.makeText(applicationContext, "Error during creating RideModel!", Toast.LENGTH_LONG).show()
            }

            dataBaseHelper = DataBaseHelper(applicationContext)
            val succes = dataBaseHelper.addOne(rideModel)
            Toast.makeText(applicationContext,"Adding ride succes: $succes", Toast.LENGTH_LONG).show()
            showRidesOnListViews()
        }

        binding.buttonDate.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, currentYear, currentMonth, currentDay ->
                myCalendar.set(Calendar.YEAR, currentYear) // ustawianie domyślnej daty
                myCalendar.set(Calendar.MONTH, currentMonth)
                myCalendar.set(Calendar.DAY_OF_MONTH, currentDay)
                selectedYear = myCalendar.get(Calendar.YEAR)
                selectedMonth = myCalendar.get(Calendar.MONTH)
                selectedDay = myCalendar.get(Calendar.DAY_OF_MONTH)
                selectedDateText = formatDate.format(myCalendar.time)
                selectedDate = myCalendar.time
                Toast.makeText(applicationContext, "Selected date: $selectedDateText", Toast.LENGTH_SHORT).show()
            }
            val dpd = DatePickerDialog(this, datePicker, selectedYear, selectedMonth, selectedDay)
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000 // ustawianie minimalnej daty do wybrania
            var yearInMilis: Long = 1000*60
            yearInMilis *= 60*24*365
            dpd.datePicker.maxDate = System.currentTimeMillis() + yearInMilis // ustawienie maksymalnej daty do wybrania na + rok
            dpd.show()
        }

        binding.buttonTime.setOnClickListener {
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{view, currentHour, currentMinute ->
                myCalendar.set(Calendar.HOUR_OF_DAY, currentHour)
                myCalendar.set(Calendar.MINUTE, currentMinute)
                selectedHour = myCalendar.get(Calendar.HOUR_OF_DAY)
                selectedMinute = myCalendar.get(Calendar.MINUTE)
                selectedTime = "$selectedHour:$selectedMinute"
            }, selectedHour, selectedMinute, true ).show()
        }

        binding.editTextTextStartPoint.setOnClickListener {

        }



        binding.buttonFind.setOnClickListener {
            var message = ""
            if (binding.editTextTextStartPoint.text.isNotEmpty()){
                message += "Start point: " + binding.editTextTextStartPoint.text + "\n"
            }
            if (binding.editTextTextDestinationPoint.text.isNotEmpty()){
                message += "Destination point: " + binding.editTextTextDestinationPoint.text + "\n"
            }
            message += "DateText: $selectedDateText \n"
            //message += "Date values : $selectedYear $selectedMonth $selectedDay \n"
            //message += "Current Date: $currentDateText \n"
            //message += "Selected date is after current day: " + selectedDate.compareTo(currentDate) + "\n"
            if(selectedDate.equals(currentDate) && (selectedHour < currentHour || (selectedHour == currentHour && selectedMinute < currentMinute))){
                // wybrano czas przed aktualną chwilą
                Toast.makeText(applicationContext, "Select later time, or different day!", Toast.LENGTH_LONG).show()
                binding.buttonTime.setBackgroundColor(Color.RED)
                binding.buttonDate.setBackgroundColor(Color.RED)
            }
            else{
                message += "Time: $selectedHour:$selectedMinute \n"
                binding.buttonTime.setBackgroundColor(Color.parseColor("#6200ee"))
                binding.buttonDate.setBackgroundColor(Color.parseColor("#6200ee"))
            }
            //message += "Current Time: $currentHour:$currentMinute \n"
            if (binding.editTextTextPassengersNumber.text.isNotEmpty()){
                message += "Passengers number: " + binding.editTextTextPassengersNumber.text + "\n"
            }
            if (binding.editTextTextPricePerPassenger.text.isNotEmpty()){
                message += "Price per passenger: " + binding.editTextTextPricePerPassenger.text + "\n"
            }
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }















    }

    @SuppressLint("NewApi")
    private fun showRidesOnListViews() {
        ridesArrayAdapter = ArrayAdapter<RideModel>(
            applicationContext,
            R.layout.simple_list_item_1,
            dataBaseHelper.getAll()
        )
        binding.listViewRides.adapter = ridesArrayAdapter
    }
}
//todo - dodanie timestamp int do klasy Ridemodel,
// zamiana daty i godziny na timestamp,
// wyszukiwanie po dacie