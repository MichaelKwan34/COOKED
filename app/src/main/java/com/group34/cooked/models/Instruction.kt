package com.group34.cooked.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Instruction(
    var stepNumber: Int = -1,
    val description: String = ""
) : Parcelable
