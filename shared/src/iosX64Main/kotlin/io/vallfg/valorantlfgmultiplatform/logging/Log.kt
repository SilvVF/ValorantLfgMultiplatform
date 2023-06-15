package io.vallfg.valorantlfgmultiplatform.logging

actual object Log {
    actual fun d(tag: String, item: String) {
        println(tag + item)
    }
}