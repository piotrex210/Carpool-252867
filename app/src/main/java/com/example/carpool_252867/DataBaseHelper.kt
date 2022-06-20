package com.example.carpool_252867
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
class DataBaseHelper: SQLiteOpenHelper {
    constructor(context: Context?) :
            super(context, "rides.db", null, 1)
    val RIDES_TABLE = "RIDES_TABLE"
    val COLUMN_ID = "ID"
    val COLUMN_START_POINT = "START_POINT"
    val COLUMN_DESTINATION_POINT = "DESTINATION_POINT"
    val COLUMN_DATE = "DATE"
    val COLUMN_TIME = "TIME"
    val COLUMN_NUMBER_OF_PASSENGERS = "NUMBER_OF_PASSENGERS"
    val COLUMN_PRICE_PER_SEAT = "PRICE_PER_SEAT"
    val COLUMN_TIMESTAMP = "TIMESTAMP"




    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatment = "CREATE TABLE "+RIDES_TABLE+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_START_POINT+" TEXT, "+COLUMN_DESTINATION_POINT+" TEXT, "+COLUMN_DATE+" TEXT, "+COLUMN_TIME+" STRING," +
                COLUMN_NUMBER_OF_PASSENGERS+" INTEGER, "+COLUMN_PRICE_PER_SEAT+" INTEGER)"
        db.execSQL(createTableStatment)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

    }
    public fun addOne(rideModel: RideModel): Boolean {
        var db = this.writableDatabase // database
        var cv = ContentValues()    // content value

        cv.put(COLUMN_START_POINT, rideModel.startPoint)
        cv.put(COLUMN_DESTINATION_POINT, rideModel.destinationPoint)
        cv.put(COLUMN_DATE, rideModel.date)
        cv.put(COLUMN_TIME, rideModel.time)
        cv.put(COLUMN_NUMBER_OF_PASSENGERS, rideModel.numberOfPassengers)
        cv.put(COLUMN_PRICE_PER_SEAT, rideModel.pricePerPassenger)
        cv.put(COLUMN_TIMESTAMP, rideModel.timestamp)

        val succes = db.insert(RIDES_TABLE, null, cv)
        return succes >= 0
    }

    public fun getAll(): List<RideModel>{
        var returnList:ArrayList<RideModel> = ArrayList<RideModel>()

        val queryString = "SELECT * FROM " + RIDES_TABLE
        var db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)

        if(cursor.moveToFirst()){
            do{
                val rideID = cursor.getInt(0)
                val startPoint = cursor.getString(1)
                val destinationPoint = cursor.getString(2)
                val date = cursor.getString(3)
                val time = cursor.getString(4)
                val numberOfPassengers = cursor.getInt(5)
                val pricePerPassenger =  cursor.getInt(6)
                val timestamp =  cursor.getLong(7)


                val newRide = RideModel(rideID, startPoint, destinationPoint,
                    date, time, numberOfPassengers, pricePerPassenger, timestamp)
                returnList.add(newRide)

            }while(cursor.moveToNext())
        }
        else{
            // do nothing
        }
        cursor.close()
        db.close()
        return returnList
    }

    public fun deleteErrors(){
        val queryString = "DELETE FROM " + RIDES_TABLE + " WHERE " + COLUMN_DATE + " = 'error'"
        var db = this.writableDatabase
        db.delete(RIDES_TABLE, COLUMN_DATE+" = 'error'", null)
        db.close()
    }

    public fun deleteAllData(){
        var db = this.writableDatabase
        db.delete(RIDES_TABLE, "ID > 0", null)
        db.close()
    }

    public fun alterTable(){
        val queryString = "ALTER TABLE " + RIDES_TABLE + " ADD COLUMN " + COLUMN_TIMESTAMP + " INTEGER DEFAULT -1"
        var db = this.writableDatabase
        db.rawQuery(queryString,null)
        db.close()
    }

}