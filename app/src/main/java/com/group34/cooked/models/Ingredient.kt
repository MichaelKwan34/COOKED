package com.group34.cooked.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Measurement(
    var name: String = "",
    var unit: String = ""
) : Parcelable

@Parcelize
data class Ingredient(
    var name: String = "",
    var quantity: Double = -1.0,
    var measurement: Measurement = Measurement()
) : Parcelable
