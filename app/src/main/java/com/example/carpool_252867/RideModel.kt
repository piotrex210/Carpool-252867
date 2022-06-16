package com.example.carpool_252867

import java.sql.Time
import java.util.*

class RideModel{
    private var id: Int
    private var startPoint: String
    private var destinationPoint: String
    private var date: Date
    private var time: String
    private var numberOfPassengers: Int
    private var pricePerPassenger: Int

    constructor(
        id: Int,
        startPoint: String,
        destinationPoint: String,
        date: Date,
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