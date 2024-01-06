package com.example.attendence

data class CounterItem(
    var id:String?=null,
    var name:String?=null,
    var attendedClass:Int?=0,
    var missedClass:Int?=0
)
