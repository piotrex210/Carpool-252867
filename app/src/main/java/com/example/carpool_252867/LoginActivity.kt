package com.example.carpool_252867

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.carpool_252867.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var dataBaseHelper: DataBaseHelper


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBaseHelper = DataBaseHelper(applicationContext)

        binding.LoginButton.setOnClickListener {
            var userId: Int
            if(binding.LoginEditText.text.isEmpty() || binding.PasswordEditText.text.isEmpty()){ // jeśli jest błąd logowania
                Toast.makeText(applicationContext,"Fill in login and password fields!",Toast.LENGTH_LONG).show()
            }
            else{
                val user = dataBaseHelper.getUserByLogin(binding.LoginEditText.text.toString())
                if(user.tel_num == -1) {// if error
                    Toast.makeText(applicationContext,"User not found!",Toast.LENGTH_LONG).show()
                }
                else if(user.password != binding.PasswordEditText.text.toString()){
                    Toast.makeText(applicationContext,"Wrong Password",Toast.LENGTH_LONG).show()
                }
                else{ // Login succed
                    userId = user.id
                    Toast.makeText(applicationContext,"Login succes!",Toast.LENGTH_LONG).show()
                    intent.putExtra("UserId", userId)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }

        binding.buttonRegister.setOnClickListener {
            if(binding.LoginEditText.text.isEmpty() || binding.PasswordEditText.text.isEmpty() || binding.RetypePasswortEditText.text.isEmpty()
                || binding.textViewTelNum.text.isEmpty())// jeśli nie wszystkie pola uzupełniono
            {
                Toast.makeText(applicationContext,"Fill in all fields!",Toast.LENGTH_LONG).show()
            }
            else{
                val user = dataBaseHelper.getUserByLogin(binding.LoginEditText.text.toString())
                if(user.tel_num != -1) {// Jeśli znalazł takiego użytkownika
                    Toast.makeText(applicationContext,"User with this login already exists! Change login!",Toast.LENGTH_LONG).show()
                }
                else{ // jeśli nie ma użytkownika o tym loginie
                    if(binding.PasswordEditText.text.toString() != binding.RetypePasswortEditText.text.toString()) // jeśli hasła się nie zgadzają
                        Toast.makeText(applicationContext,"Retype the same password!",Toast.LENGTH_LONG).show()
                    else{
                        var userModel = UserModel(-1, binding.LoginEditText.text.toString(), binding.PasswordEditText.text.toString(), binding.editTextTelephoneNumber.text.toString().toInt())
                        dataBaseHelper.addOne(userModel)
                        val userId = dataBaseHelper.getLastAddedUserId()
                        Toast.makeText(applicationContext,"Registration succes! Id:$userId",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.buttonBack.setOnClickListener {
            intent.putExtra("UserId", -1)
            setResult(RESULT_OK, intent)
            finish()
        }




















    }
}