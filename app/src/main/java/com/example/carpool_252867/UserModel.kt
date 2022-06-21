package com.example.carpool_252867

class UserModel {
    var id: Int
    var login: String
    var password: String
    var tel_num: Int

    constructor(id: Int, login: String, password: String, tel_num: Int) {
        this.id = id
        this.login = login
        this.password = password
        this.tel_num = tel_num
    }
    override fun toString(): String{
        return "UserModel: id:$id, login:$login, password:$password, tel_num:$tel_num"
    }
}