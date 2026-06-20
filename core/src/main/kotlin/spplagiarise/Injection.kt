package spplagiarise

import jakarta.enterprise.inject.spi.CDI

inline fun <reified T : Any> inject(): T {
    return CDI.current().select(T::class.java).get()
}