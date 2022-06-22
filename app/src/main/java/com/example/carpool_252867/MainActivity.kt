package com.example.carpool_252867

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.carpool_252867.databinding.ActivityMainBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

const val REQUEST_LOGIN_ACTIVITY = 1

var formatDateAndTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    SimpleDateFormat("dd MMMM YYYY, HH:mm", Locale.UK)
} else {
    TODO("VERSION.SDK_INT < N")
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var ridesArrayAdapter:ArrayAdapter<RideModel>

    var currentUserId = -1
    lateinit var currentUser: UserModel

    @SuppressLint("NewApi")
    lateinit var dataBaseHelper: DataBaseHelper

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBaseHelper = DataBaseHelper(applicationContext)

        //dataBaseHelper.alterTable()
        //dataBaseHelper.deleteAllData()
        dataBaseHelper.deleteErrors()
        showRidesOnListViews()

        val myCalendar = Calendar.getInstance()
        val formatDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd MMMM YYYY", Locale.UK)
        } else {
            TODO("VERSION.SDK_INT < N")
        }

        //val currentHour = myCalendar.get(Calendar.HOUR_OF_DAY)
        //val currentMinute = myCalendar.get(Calendar.MINUTE)
        val selectedDay = myCalendar.get(Calendar.DAY_OF_MONTH)
        val selectedMonth = myCalendar.get(Calendar.MONTH)
        val selectedYear = myCalendar.get(Calendar.YEAR)
        val selectedHour = myCalendar.get(Calendar.HOUR_OF_DAY)
        val selectedMinute = myCalendar.get(Calendar.MINUTE)
        //var currentDateText = formatDate.format(myCalendar.time)
        //var selectedDateText  = formatDate.format(myCalendar.time)//= formatDate.format(myCalendar.time)
        var selectedDate = myCalendar.time
        //var currentDate = myCalendar.time
        //var selectedTime = ""
        val currentTimestamp = Timestamp(myCalendar.timeInMillis - myCalendar.timeInMillis%60000)
        var timestamp = Timestamp(myCalendar.timeInMillis - myCalendar.timeInMillis%60000)


        binding.buttonViewAll.setOnClickListener {
            //val allRides = dataBaseHelper.getAll()
            dataBaseHelper.deleteErrors()
            showRidesOnListViews()
        }

        binding.buttonOffer.setOnClickListener {
            var rideModel:RideModel
            if(timestamp.after(currentTimestamp) && binding.editTextTextDestinationPoint.text.isNotEmpty() &&
                binding.editTextTextStartPoint.text.isNotEmpty()  // jeśli wybrano dobrą datę, start i koniec
                 && binding.editTextTextPricePerPassenger.text.isNotEmpty() &&
                                    binding.editTextTextPricePerPassenger.text.isNotEmpty() && // jeśli wybrano dobrą datę i resztę danych
                    currentUserId != -1) { // jeśli użytkownik jest zalogowany
                try {
                    rideModel = RideModel(
                        -1,
                        binding.editTextTextStartPoint.text.toString(),
                        binding.editTextTextDestinationPoint.text.toString(),
                        Integer.parseInt(binding.editTextTextPassengersNumber.text.toString()),
                        Integer.parseInt(binding.editTextTextPricePerPassenger.text.toString()),
                        timestamp.time,
                        currentUserId
                    )
                } catch (e: Exception) {
                    rideModel = RideModel(
                        -1, "error", "error",
                        -1, -1, -1, -1
                    )
                    Toast.makeText(
                        applicationContext,
                        "Error during creating RideModel!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                dataBaseHelper = DataBaseHelper(applicationContext)
                val succes = dataBaseHelper.addOne(rideModel)
                Toast.makeText(applicationContext, "Adding ride succes: $succes", Toast.LENGTH_LONG)
                    .show()
                showRidesOnListViews()
                binding.buttonTime.setBackgroundColor(Color.parseColor("#6200ee"))
                binding.buttonDate.setBackgroundColor(Color.parseColor("#6200ee"))
            }
            else if(currentUserId == -1){
                Toast.makeText(applicationContext, "You have to log in to offer a ride!", Toast.LENGTH_LONG).show()
            }
            else if(timestamp.before(currentTimestamp)){ // jeśli wybrano czas wcześniejszy niż teraz
                Toast.makeText(applicationContext, "Select later time, or different day!", Toast.LENGTH_LONG).show()
                binding.buttonTime.setBackgroundColor(Color.RED)
                binding.buttonDate.setBackgroundColor(Color.RED)
            }
            else{
                Toast.makeText(applicationContext, "Make sure You provided correct data!", Toast.LENGTH_LONG).show()
            }

        }

        binding.buttonDate.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, currentYear, currentMonth, currentDay ->
                myCalendar.set(Calendar.YEAR, currentYear) // ustawianie domyślnej daty
                myCalendar.set(Calendar.MONTH, currentMonth)
                myCalendar.set(Calendar.DAY_OF_MONTH, currentDay)
                myCalendar.set(Calendar.SECOND, 0)
                selectedDate = myCalendar.time
                timestamp = Timestamp(myCalendar.timeInMillis - myCalendar.timeInMillis%60000)
                Toast.makeText(applicationContext, "Selected date: $timestamp", Toast.LENGTH_SHORT).show()
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
                myCalendar.set(Calendar.SECOND, 0)
                timestamp = Timestamp(myCalendar.timeInMillis - myCalendar.timeInMillis%60000)
                val str = formatDateAndTime.format(Date(timestamp.time))
                Toast.makeText(applicationContext, str, Toast.LENGTH_LONG).show()
            }, selectedHour, selectedMinute, true ).show()
        }

        binding.buttonFind.setOnClickListener {

            if(!timestamp.before(currentTimestamp) && binding.editTextTextDestinationPoint.text.isNotEmpty() &&
                binding.editTextTextStartPoint.text.isNotEmpty() ) { // jeśli wybrano dobrą datę, start i koniec
                    // && binding.editTextTextPricePerPassenger.text.isDigitsOnly() &&
                //                    binding.editTextTextPricePerPassenger.text.isDigitsOnly()
                showSelectedRidesOnListViews(start = binding.editTextTextStartPoint.text.toString(),
                    dest = binding.editTextTextDestinationPoint.text.toString(), time = timestamp.time)
                //Toast.makeText(applicationContext, "Sukces wyświetlania! start:"+binding.editTextTextStartPoint.text.toString()+
                        //"dest="+binding.editTextTextDestinationPoint.text.toString()+ "time="+timestamp.time, Toast.LENGTH_LONG).show()
            }
            else{
                //var message = "Porażka wyświetlania"
                //Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
            Toast.makeText(applicationContext, "Timestamp: $timestamp, currentTimestamp: $currentTimestamp", Toast.LENGTH_LONG).show()


        }
        binding.loginRegisterButton.setOnClickListener {
            var userId: Int
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, REQUEST_LOGIN_ACTIVITY)
        }

        binding.listViewRides.setOnItemClickListener { parent, view, position, id ->
            if(currentUserId != -1){
                val selectedRide: RideModel = parent.getItemAtPosition(position) as RideModel
                if(selectedRide.driverId != currentUserId){
                    dataBaseHelper.assignPassengerToRide(selectedRide.id, currentUserId)
                    Toast.makeText(applicationContext, "Succesful choosing ride!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(applicationContext, "You can not choose Your own ride!", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(applicationContext,   "First log in to choose a ride!", Toast.LENGTH_SHORT).show()
            }

        }

        binding.UsernameTextView.setOnClickListener(){
            if(currentUserId != -1){
                val intent = Intent(this, MyAccountActivity::class.java)
                intent.putExtra("userId", currentUserId)
                startActivity(intent)
            }
        }
















    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN_ACTIVITY && resultCode == RESULT_OK) {
            var loginData = data?.extras?.getInt("UserId")
            if (loginData != null) {
                currentUserId = loginData
            }
            Toast.makeText(applicationContext, "UserId: $currentUserId", Toast.LENGTH_LONG)
                .show()
            if(currentUserId != -1){ // jeśli zalogowano użytkownika
                currentUser = dataBaseHelper.getUserById(currentUserId)
                binding.UsernameTextView.text ="My Account:${currentUser.login}"
            }
        }
        else{
            Toast.makeText(applicationContext, "Login data not found", Toast.LENGTH_LONG).show()
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
    @SuppressLint("NewApi")
    private fun showSelectedRidesOnListViews(start: String, dest: String, time: Long) {
        ridesArrayAdapter = ArrayAdapter<RideModel>(
            applicationContext,
            R.layout.simple_list_item_1,
            dataBaseHelper.selectRidesWhere(start, dest, time)
        )
        binding.listViewRides.adapter = ridesArrayAdapter
    }
}


// todo - wyświetlanie nowego okna z danymi użytkownika i listą jego ofert
// todo - zmienny layout dla różnych rozmiarów wyświetlacza
// todo - dokumentacja