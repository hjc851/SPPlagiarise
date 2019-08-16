package spplagiarise.config

import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class Configuration {
    lateinit var root: Path
    lateinit var inputRoot: Path
    lateinit var outputRoot: Path
    lateinit var libs: List<Path>
    var randomSeed: Long = 0
    var copies: Int = 1
    var extreme: Boolean = false
}