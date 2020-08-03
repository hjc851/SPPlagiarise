package spplagiarise.util

import org.apache.commons.lang3.RandomStringUtils
import spplagiarise.config.Configuration
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

interface IRandomGenerator {
    fun randomBoolean(): Boolean
    fun randomInt(): Int
    fun randomChar(): Char
    fun randomDouble(): Double
    fun randomString(): String
    fun randomIndex(size: Int): Int
    fun <T> randomIndex(list: List<T>): T

    fun setSeed(seed: Long)
    fun setWeight(weight: Int)
}

@Singleton
class RandomGenerator: IRandomGenerator {
    private var random = Random()

    private var weight: Int = 50

    override fun setSeed(seed: Long) {
        this.random = Random(seed)
    }

    override fun setWeight(weight: Int) {
        this.weight = weight
    }

    override fun randomBoolean(): Boolean {
        val value = random.nextInt(100)
        return value < weight
    }

    override fun <T> randomIndex(list: List<T>): T {
        return list[random.nextInt(list.size)]
    }

    override fun randomIndex(size: Int): Int {
        return random.nextInt(size)
    }

    override fun randomInt(): Int {
        return random.nextInt()
    }

    override fun randomChar(): Char {
        val char = RandomStringUtils.randomAlphanumeric(1)[0]
        return char
    }

    override fun randomDouble(): Double {
        return random.nextDouble()
    }

    override fun randomString(): String {
        return RandomStringUtils.randomAlphanumeric(abs(random.nextInt(20)))
    }
}