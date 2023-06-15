package io.vallfg.valorantlfgmultiplatform.logging

import android.util.Log

actual object Log {
    actual fun d(tag: String, item: String) {
        Log.d(tag, item)
    }
}