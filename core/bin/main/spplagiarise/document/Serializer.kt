package spplagiarise.document

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Path
import javax.inject.Singleton
import kotlin.reflect.KClass

interface ISerializer {
    fun <T : Any> read(path: Path, kls: KClass<T>): T
    fun <T> write(path: Path, document: T)
}

@Singleton
class Serializer : ISerializer {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
            .setDefaultPrettyPrinter(DefaultPrettyPrinter())
            .enable(SerializationFeature.INDENT_OUTPUT)

    override fun <T : Any> read(path: Path, kls: KClass<T>): T {
        return mapper.readValue(path.toFile(), kls.java)
    }

    override fun <T> write(path: Path, document: T) {
        mapper.writeValue(path.toFile(), document)
    }
}