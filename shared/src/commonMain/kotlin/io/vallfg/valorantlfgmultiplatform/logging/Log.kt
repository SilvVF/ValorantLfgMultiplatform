package io.vallfg.valorantlfgmultiplatform.logging

/**
 * Object that allows Logging to the logcat from the shared module.
 * This uses Log.d() to print to the log cat on android.
 * On ios this will call [println].
 */
expect object Log {

    fun d(tag: String, item: String)
}