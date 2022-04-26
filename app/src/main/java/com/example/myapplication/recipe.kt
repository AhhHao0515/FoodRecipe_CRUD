package com.example.myapplication

data class recipe (
    val id : String,
    val name : String,
    val step : String,
    val mat : String
){
    constructor(): this("","","", ""){

    }
}