package com.example.carpool_252867

import android.R
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.carpool_252867.databinding.ActivityLoginBinding
import com.example.carpool_252867.databinding.ActivityMyAccountBinding

class MyAccountActivity : AppCompatActivity() {
    var currentUserId:Int = -1
    private lateinit var binding: ActivityMyAccountBinding
    lateinit var dataBaseHelper: DataBaseHelper
    lateinit var driverRidesArrayAdapter: ArrayAdapter<RideModel>
    lateinit var passengerRidesArrayAdapter:ArrayAdapter<RideModel>

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataBaseHelper = DataBaseHelper(applicationContext)

        currentUserId = intent.extras!!.getInt("userId")
        Toast.makeText(applicationContext, "UserId$currentUserId", Toast.LENGTH_SHORT).show()

        binding.textViewUsername.text = dataBaseHelper.getUserById(currentUserId).login

        showDriverRidesOnListViews()
        showPassengerRidesOnListViews()

        binding.buttonBackFromMyAddount.setOnClickListener(){
            setResult(RESULT_OK, intent)
            finish()
        }














    }
    @SuppressLint("NewApi")
    private fun showPassengerRidesOnListViews() {
        passengerRidesArrayAdapter = ArrayAdapter<RideModel>(
            applicationContext,
            R.layout.simple_list_item_1,
            dataBaseHelper.selectRidesWherePassengerId(currentUserId)
        )
        binding.listViewPassengerRides.adapter = passengerRidesArrayAdapter
    }
    @SuppressLint("NewApi")
    private fun showDriverRidesOnListViews() {
        driverRidesArrayAdapter = ArrayAdapter<RideModel>(
            applicationContext,
            R.layout.simple_list_item_1,
            dataBaseHelper.selectRidesWhereDriverId(currentUserId)
        )
        binding.listViewDriverRides.adapter = driverRidesArrayAdapter
    }
}