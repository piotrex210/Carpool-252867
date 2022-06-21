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
    val COLUMN_NUMBER_OF_PASSENGERS = "NUMBER_OF_PASSENGERS"
    val COLUMN_PRICE_PER_SEAT = "PRICE_PER_SEAT"
    val COLUMN_TIMESTAMP = "TIMESTAMP"
    val COLUMN_DRIVER_ID = "DRIVER_ID"
    val COLUMN_PASSENGER_ID = "PASSENGER_ID"

    val USERS_TABLE = "USERS_TABLE"
    val COLUMN_USER_ID = "ID"
    val COLUMN_LOGIN = "LOGIN"
    val COLUMN_PASSWORD = "PASSWORD"
    val COLUMN_TEL_NUM = "TEL_NUM"

    override fun onCreate(db: SQLiteDatabase) {
        var createTableStatment = "CREATE TABLE "+USERS_TABLE+"("+COLUMN_USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOGIN+" TEXT, "+COLUMN_PASSWORD+" TEXT, " +COLUMN_TEL_NUM+" INTEGER)"
        db.execSQL(createTableStatment)
        createTableStatment = "CREATE TABLE "+RIDES_TABLE+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_START_POINT+" TEXT, "+COLUMN_DESTINATION_POINT+" TEXT, " +
                COLUMN_NUMBER_OF_PASSENGERS+" INTEGER, "+COLUMN_PRICE_PER_SEAT+" INTEGER, "+
                COLUMN_TIMESTAMP+ " INTEGER, " + COLUMN_DRIVER_ID+" INTEGER, " + COLUMN_PASSENGER_ID + " INTEGER," +
                "FOREIGN KEY ("+COLUMN_DRIVER_ID+") REFERENCES "+USERS_TABLE+"("+COLUMN_ID+"),"+
                "FOREIGN KEY ("+COLUMN_PASSENGER_ID+") REFERENCES "+USERS_TABLE+"("+COLUMN_ID+"))"
        db.execSQL(createTableStatment)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

    }
    public fun addOne(rideModel: RideModel): Boolean {
        var db = this.writableDatabase // database
        var cv = ContentValues()    // content value

        cv.put(COLUMN_START_POINT, rideModel.startPoint)
        cv.put(COLUMN_DESTINATION_POINT, rideModel.destinationPoint)
        cv.put(COLUMN_NUMBER_OF_PASSENGERS, rideModel.numberOfPassengers)
        cv.put(COLUMN_PRICE_PER_SEAT, rideModel.pricePerPassenger)
        cv.put(COLUMN_TIMESTAMP, rideModel.timestamp)

        val succes = db.insert(RIDES_TABLE, null, cv)
        return succes >= 0
    }

    public fun addOne(userModel: UserModel): Boolean{
        var db = this.writableDatabase // database
        var cv = ContentValues()    // content value

        cv.put(COLUMN_LOGIN, userModel.login)
        cv.put(COLUMN_PASSWORD, userModel.password)
        cv.put(COLUMN_TEL_NUM, userModel.tel_num)

        val succes = db.insert(USERS_TABLE, null, cv)
        return succes >= 0
    }

    public fun getLastAddedUserId(): Int{
        val queryString = "SELECT * FROM "+USERS_TABLE
        var db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        var returnId: Int
        if(cursor.moveToLast()) {
            returnId = cursor.getInt(0)
        }
        else{
            returnId = -1
        }
        cursor.close()
        db.close()
        return returnId
    }

    public fun getAll(): List<RideModel>{
        var returnList:ArrayList<RideModel> = ArrayList<RideModel>()

        val queryString = "SELECT * FROM " + RIDES_TABLE
        var db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)

        if(cursor.moveToLast()){
            do{
                val rideID = cursor.getInt(0)
                val startPoint = cursor.getString(1)
                val destinationPoint = cursor.getString(2)
                val numberOfPassengers = cursor.getInt(3)
                val pricePerPassenger =  cursor.getInt(4)
                val timestamp =  cursor.getLong(5)
                val newRide = RideModel(rideID, startPoint, destinationPoint,
                    numberOfPassengers, pricePerPassenger, timestamp)
                returnList.add(newRide)

            }while(cursor.moveToPrevious())
        }
        else{
            // do nothing
        }
        cursor.close()
        db.close()
        return returnList
    }

    public fun selectRidesWhere(start: String, dest:String, time:Long): List<RideModel>{
        var returnList:ArrayList<RideModel> = ArrayList<RideModel>()
        val queryString = "SELECT * FROM " + RIDES_TABLE + " WHERE " + COLUMN_START_POINT + "= '"+start+"' "+
                "AND " + COLUMN_DESTINATION_POINT + " = '"+dest+"' AND " + COLUMN_TIMESTAMP + " >= "+time.toString()

        var db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)


        if(cursor.moveToLast()){
            do{
                val rideID = cursor.getInt(0)
                val startPoint = cursor.getString(1)
                val destinationPoint = cursor.getString(2)
                val numberOfPassengers = cursor.getInt(3)
                val pricePerPassenger =  cursor.getInt(4)
                val timestamp =  cursor.getLong(5)
                val newRide = RideModel(rideID, startPoint, destinationPoint,
                    numberOfPassengers, pricePerPassenger, timestamp)
                returnList.add(newRide)

            }while(cursor.moveToPrevious())
        }
        else{
            // do nothing
        }
        cursor.close()
        db.close()
        return returnList
    }

    public fun deleteErrors(){
        val queryString = "DELETE FROM " + RIDES_TABLE + " WHERE " + COLUMN_START_POINT + " = 'error'"
        var db = this.writableDatabase
        db.delete(RIDES_TABLE, COLUMN_ID+" = -1", null)
        db.close()
    }

    public fun deleteAllData(){
        var db = this.writableDatabase
        db.delete(RIDES_TABLE, "ID > -2", null)
        db.close()
    }

    public fun alterTable(){
        val queryString = "ALTER TABLE " + RIDES_TABLE + " ADD COLUMN " + COLUMN_TIMESTAMP + " INTEGER DEFAULT -1"
        var db = this.writableDatabase
        db.rawQuery(queryString,null)
        db.close()
    }

}