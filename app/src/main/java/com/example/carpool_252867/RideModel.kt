package com.example.carpool_252867

import java.sql.Time
import java.util.*

class RideModel{
     var id: Int
     var startPoint: String
     var destinationPoint: String
     var date: String
     var time: String
     var numberOfPassengers: Int
     var pricePerPassenger: Int

    constructor(
        id: Int,
        startPoint: String,
        destinationPoint: String,
        date: String,
        time: String,
        numberOfPassengers: Int,
        pricePerPassenger: Int
    ) {
        this.id = id
        this.startPoint = startPoint
        this.destinationPoint = destinationPoint
        this.date = date
        this.time = time
        this.numberOfPassengers = numberOfPassengers
        this.pricePerPassenger = pricePerPassenger
    }


    override fun toString(): String {
        return "RideModel(id=$id, startPoint='$startPoint', destinationPoint='$destinationPoint', date=$date, time=$time, numberOfPassengers=$numberOfPassengers, pricePerPassenger=$pricePerPassenger)"
    }

}