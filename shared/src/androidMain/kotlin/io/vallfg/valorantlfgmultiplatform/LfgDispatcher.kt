package io.vallfg.valorantlfgmultiplatform

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class LfgDispatcher actual constructor(
    default: CoroutineDispatcher,
    main: CoroutineDispatcher
) {

    actual val io = Dispatchers.IO
}