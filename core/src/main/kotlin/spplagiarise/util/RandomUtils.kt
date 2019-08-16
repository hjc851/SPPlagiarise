package spplagiarise.util

import spplagiarise.config.Configuration
import org.apache.commons.lang3.RandomStringUtils
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
}

class RandomGenerator(seed: Long) : IRandomGenerator {
    private val random = Random(seed)

    override fun randomBoolean(): Boolean {
        return random.nextBoolean()
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

@ApplicationScoped
class RandomGeneratorProducer {
    @Inject
    private lateinit var config: Configuration

    @Produces
    @Singleton
    fun produceRandomGenerator(): RandomGenerator {
        return RandomGenerator(config.randomSeed)
    }
}