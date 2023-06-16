package io.vallfg.valorantlfgmultiplatform.android.theme

import androidx.annotation.ColorInt
import androidx.annotation.Size
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val DarkBluishBlack = Color(0xff0F1923)
val DarkBackGround = Color(0xFF0A1018)
val BluishGray = Color(0xff1B2733)
val LightBluishGray = Color(0xff263747)
val LightGray = Color(0xff94A6BC)
val Sky500 = Color(0xff0ea5e9)

@ColorInt
fun parseColor(@Size(min = 1) colorString: String): Int {
    if (colorString[0] == '#') { // Use a long to avoid rollovers on #ffXXXXXX
        var color = colorString.substring(1).toLong(16)
        if (colorString.length == 7) { // Set the alpha value
            color = color or -0x1000000
        } else require(colorString.length == 9) { "Unknown color" }
        return color.toInt()
    }
    throw IllegalArgumentException("Unknown color")
}