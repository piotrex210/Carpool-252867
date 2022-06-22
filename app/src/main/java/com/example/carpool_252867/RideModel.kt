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
     var driverId: Int
     var passengerId: Int


    constructor(
        id: Int,
        startPoint: String,
        destinationPoint: String,
        numberOfPassengers: Int,
        pricePerPassenger: Int,
        timestamp: Long,
        driverId: Int,
        passengerId: Int
    ) {
        this.id = id
        this.startPoint = startPoint
        this.destinationPoint = destinationPoint
        this.numberOfPassengers = numberOfPassengers
        this.pricePerPassenger = pricePerPassenger
        this.timestamp = timestamp
        this.driverId = driverId
        this.passengerId = passengerId
    }

    constructor(
        id: Int,
        startPoint: String,
        destinationPoint: String,
        numberOfPassengers: Int,
        pricePerPassenger: Int,
        timestamp: Long,
        driverId: Int,
    ) {
        this.id = id
        this.startPoint = startPoint
        this.destinationPoint = destinationPoint
        this.numberOfPassengers = numberOfPassengers
        this.pricePerPassenger = pricePerPassenger
        this.timestamp = timestamp
        this.driverId = driverId
        this.passengerId = -1
    }


    override fun toString(): String {
        return "RideModel(id=$id, startPoint='$startPoint', destinationPoint='$destinationPoint', date= "+
                formatDateAndTime.format(Date(timestamp)) + ", numberOfPassengers=$numberOfPassengers, " +
                "pricePerPassenger=$pricePerPassenger), driverId: $driverId, passengerId: $passengerId"
    }

}