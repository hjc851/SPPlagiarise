package spplagiarise.naming

import org.apache.commons.lang3.StringUtils
import spplagiarise.ast.IdentifierVisitor
import spplagiarise.cdi.ObfuscatorScoped
import spplagiarise.config.Configuration
import spplagiarise.synonyms.ISynonymClient
import spplagiarise.util.IRandomGenerator
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@ObfuscatorScoped
open class DeferredNameMappingContext @Inject constructor(identifierVisitor: IdentifierVisitor) {

    @Inject
    private lateinit var nameContext: DeferredNameContext

    @Inject
    private lateinit var client: ISynonymClient

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    private var reservedIds: MutableSet<String> = identifierVisitor.identifiers.toMutableSet()

    private val mappings = mutableMapOf<Int, String>()

    private var mappedNameCounter = AtomicInteger(0)

    open fun getMappingCount(): Int {
        return mappedNameCounter.get()
    }

    open fun generateRandomName(nameBase: String): String {
        val synonyms = client.getSynonymsForTerm(nameBase)

        if (synonyms.isEmpty()) {
            reservedIds.add(nameBase)
            return nameBase
        }

        val matches = synonyms.filterNot { reservedWords.contains(it) }
                .filterNot { reservedIds.contains(it) }
                .filterNot { mappings.values.contains(it) }
                .filterNot { javaLangClasses.contains(it) }
                .filter { it.isNotEmpty() }
                .filter { it.first().isLetter() }

        val name = if (matches.isEmpty()) random.randomIndex(synonyms)
        else random.randomIndex(matches)

        reservedIds.add(name)
        return name
    }

    open fun getMappedName(id: Int, ucFirst: Boolean): String {
        return mappings.getOrPut(id) {
            if (config.extreme || random.randomBoolean()) {
                mappedNameCounter.incrementAndGet()
                return@getOrPut convertId(id, ucFirst)
            } else {
                return@getOrPut getOriginalName(id)
            }
        }
    }

    private fun getOriginalName(id: Int): String {
        val binding = nameContext.bindingForId(id)
        val originalName = binding.name
        return originalName
    }

    private fun convertId(id: Int, ucFirst: Boolean): String {
        val binding = nameContext.bindingForId(id)
        val originalNameComponents = binding.name.split(".")

        // Do the API call
        val newNameComponents = originalNameComponents.map { term ->
            val synonyms = client.getSynonymsForTerm(term)
                    .filterNot { reservedWords.contains(it) }
                    .filterNot { reservedIds.contains(it) }
                    .filterNot { mappings.values.contains(it) }
                    .filter { it.isNotEmpty() }
                    .filter { it.first().isLetter() }

            return@map if (synonyms.isEmpty()) term
            else random.randomIndex(synonyms)
        }.toMutableList()

        if (newNameComponents.size > 0 && ucFirst)
            newNameComponents[newNameComponents.size - 1] = StringUtils.capitalize(newNameComponents.last())

        // Return it
        return newNameComponents.joinToString(".")
    }
}

private val reservedWords = listOf(
        "abstract",
        "boolean",
        "break",
        "byte",
        "case",
        "catch",
        "char",
        "class",
        "const",
        "continue",
        "default",
        "do",
        "double",
        "else",
        "extends",
        "final",
        "finally",
        "float",
        "for",
        "goto",
        "if",
        "implements",
        "import",
        "instanceof",
        "int",
        "interface",
        "long",
        "native",
        "new",
        "null",
        "package",
        "private",
        "protected",
        "public",
        "return",
        "short",
        "static",
        "super",
        "switch",
        "synchronized",
        "this",
        "throw",
        "throws",
        "transient",
        "try",
        "void",
        "volatile",
        "while",
        "assert",
        "enum",
        "strictfp"
)

val javaLangClasses = listOf(
        "Appendable",
        "AutoCloseable",
        "CharSequence",
        "Cloneable",
        "Comparable",
        "Iterable",
        "Readable",
        "Runnable",
        "Thread",
        "UncaughtExceptionHandler",
        "Boolean",
        "Double",
        "Character",
        "Subset",
        "UnicodeBlock",
        "Class",
        "ClassLoader",
        "ClassValue",
        "Compiler",
        "Double",
        "Enum",
        "Float",
        "InheritableThreadLocal",
        "Integer",
        "Long",
        "Math",
        "Number",
        "Object",
        "Package",
        "Process",
        "ProcessBuilder",
        "Runtime",
        "RuntimePermission",
        "SecurityManager",
        "Short",
        "StackTraceElement",
        "StrictMath",
        "String",
        "StringBuffer",
        "StringBuilder",
        "System",
        "Thread",
        "ThreadGroup",
        "ThreadLocal",
        "Throwable",
        "Void",
        "Timer",
        "Compiler"
)