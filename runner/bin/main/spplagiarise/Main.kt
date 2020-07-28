package spplagiarise

import javax.enterprise.inject.se.SeContainerInitializer

fun main(args: Array<String>) {
    try {
        val initialiser = SeContainerInitializer.newInstance()
        val container = initialiser.initialize()

        val application = container.select(Application::class.java).get()
        application.run(args)

        container.close()
    } catch (e: Exception) {
        println(e.message)
        println()
        e.printStackTrace()
    }
}