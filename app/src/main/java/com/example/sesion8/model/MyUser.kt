package com.example.sesion8.model

class MyUser {
    lateinit var  name : String
    lateinit var lastName : String
    var age : Int = 0

    constructor()
    constructor(name: String, lastName: String, age: Int) {
        this.name = name
        this.lastName = lastName
        this.age = age
    }

    override fun toString(): String {
        return "$name $lastName -> $age"
    }

}