package spplagiarise.dst

import org.eclipse.jdt.core.dom.Modifier

//
//  Utilities
//

//  Types types

enum class TypeDeclarationType {
    CLASS,
    INTERFACE;
}

//  Access Modifiers

enum class AccessModifier {
    PUBLIC,
    PRIVATE,
    PROTECTED,
    PACKAGEPROTECTED;

    fun text(): String {
        if (this != PACKAGEPROTECTED) {
            return this.name.toLowerCase() + " "
        }
        return ""
    }
}

object AccessModifierFactory {
    fun get(modifier: Int): AccessModifier {
        return when {
            Modifier.isPublic(modifier) -> AccessModifier.PUBLIC
            Modifier.isPrivate(modifier) -> AccessModifier.PRIVATE
            Modifier.isProtected(modifier) -> AccessModifier.PROTECTED
            else -> AccessModifier.PACKAGEPROTECTED
        }
    }
}

//  Field Modifiers

class FieldModifier(
        val isTransient: Boolean,
        val isVolatile: Boolean,
        val isStatic: Boolean,
        val isStrict: Boolean,
        val isFinal: Boolean
) {
    fun text(): String {
        val builder = StringBuilder()

        if (isTransient)
            builder.append("transient ")

        if (isVolatile)
            builder.append("volatile ")

        if (isStatic)
            builder.append("static ")

        if (isStrict)
            builder.append("strictfp ")

        if (isFinal)
            builder.append("final ")

        return builder.toString()
    }
}

object FieldModifierFactory {
    fun get(modifier: Int): FieldModifier {
        return FieldModifier(
                Modifier.isTransient(modifier),
                Modifier.isVolatile(modifier),
                Modifier.isStatic(modifier),
                Modifier.isStrictfp(modifier),
                Modifier.isFinal(modifier)
        )
    }
}

//  Method Modifiers

class MethodModifier(
        var isAbstract: Boolean,
        var isSynchronised: Boolean,
        val isFinal: Boolean,
        val isStrict: Boolean,
        val isStatic: Boolean
) {
    fun text(): String {
        val builder = StringBuilder()

        if (isAbstract)
            builder.append("abstract ")

        if (isSynchronised)
            builder.append("synchronized ")

        if (isStrict)
            builder.append("strictfp ")

        if (isStatic)
            builder.append("static ")

        if (isFinal)
            builder.append("final ")

        return builder.toString()
    }
}

object MethodModifierFactory {
    fun get(modifier: Int): MethodModifier {
        return MethodModifier(
                Modifier.isAbstract(modifier),
                Modifier.isSynchronized(modifier),
                Modifier.isFinal(modifier),
                Modifier.isStrictfp(modifier),
                Modifier.isStatic(modifier)
        )
    }
}