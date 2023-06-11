package io.vallfg.valorantlfgmultiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform