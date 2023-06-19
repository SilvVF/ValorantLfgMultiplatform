@file:OptIn(ExperimentalCoroutinesApi::class)

package io.vallfg.valorantlfgmultiplatform

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext

expect class LfgDispatcher(
    default: CoroutineDispatcher = Dispatchers.Default,
    main: CoroutineDispatcher = Dispatchers.Main
) {
    val io: CoroutineDispatcher
}