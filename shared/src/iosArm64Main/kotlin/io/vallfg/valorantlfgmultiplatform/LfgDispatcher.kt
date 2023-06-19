package io.vallfg.valorantlfgmultiplatform

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext

actual class LfgDispatcher actual constructor(
    default: CoroutineDispatcher,
    main: CoroutineDispatcher
) {

    actual val io: CoroutineDispatcher = newFixedThreadPoolContext(300, "IO")
}