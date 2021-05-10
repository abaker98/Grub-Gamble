package com.example.grubgamble

class Restaurant {

    var rName: String = ""

    var rPhone: String = ""

    var rPrice: String = ""

    var rId: Long = 0

    var rCuisine: ArrayList<String> = ArrayList()

    var rLatitude: Double = 0.0

    var rLongitude: Double = 0.0

    override fun toString(): String {
        return "$rName, $rPhone, $rPrice, $rId, $rLatitude, $rLongitude, $rCuisine"
    }
}