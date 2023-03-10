package com.example.zavrsni_rad

data class MyLocation(
    var id:String="",
    var name:String="",
    var category:String="",
    var latitude:Double?=null,
    var longitude:Double?=null,
    var image1:String="",
    var image2:String="",
    var accessibilityAverage:Double?=null,
    var excitementAverage:Double?=null,
    var originalityAverage:Double?=null,
    var photogenicAverage:Double?=null,
    var timeWorthAverage:Double?=null
)