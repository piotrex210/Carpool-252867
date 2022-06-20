package com.example.carpool_252867

import java.sql.Time
import java.util.*

class RideModel{
     var id: Int
     var startPoint: String
     var destinationPoint: String
     var numberOfPassengers: Int
     var pricePerPassenger: Int
     var timestamp: Long



    constructor(
        id: Int,
        startPoint: String,
        destinationPoint: String,
        numberOfPassengers: Int,
        pricePerPassenger: Int,
        timestamp: Long
    ) {
        this.id = id
        this.startPoint = startPoint
        this.destinationPoint = destinationPoint
        this.numberOfPassengers = numberOfPassengers
        this.pricePerPassenger = pricePerPassenger
        this.timestamp = timestamp
    }


    override fun toString(): String {
        return "RideModel(id=$id, startPoint='$startPoint', destinationPoint='$destinationPoint', date= "+ formatDateAndTime.format(Date(timestamp)) + " timestamp: " + timestamp +", numberOfPassengers=$numberOfPassengers, pricePerPassenger=$pricePerPassenger)"
    }

}