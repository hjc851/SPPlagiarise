package spplagiarise.obfuscation.l3

import org.eclipse.jdt.core.dom.ITypeBinding
import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VariableDeclarationAssignDefaultValue : DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTLocalVariableDeclarationGroup(node: DSTLocalVariableDeclarationGroup) {
        if (!config.extreme && !random.randomBoolean()) return
        for (vardecl in node.variables) {
            if (vardecl.initialiser == null) {
                analyticContext.makeModification(this::class)
                vardecl.initialiser = defaultValueFor(node.type.binding)
            }
        }
    }

    override fun visitDSTFieldGroup(node: DSTFieldGroup) {
        if (!config.extreme && !random.randomBoolean()) return
        for (field in node.fields) {
            if (field.initialiser == null && !node.modifiers.isFinal) {
                analyticContext.makeModification(this::class)
                field.initialiser = defaultValueFor(node.type.binding)
            }
        }
    }

    private fun defaultValueFor(type: ITypeBinding): DSTLiteral {
        return when {
            type.qualifiedName == "char" -> DSTCharLiteral(type, "'" + (0).toChar() + "'")
            type.qualifiedName == "boolean" -> DSTBooleanLiteral(type, false)
            type.qualifiedName == "byte" -> DSTNumberLiteral(type, "0")
            type.qualifiedName == "short" -> DSTNumberLiteral(type, "0")
            type.qualifiedName == "int" -> DSTNumberLiteral(type, "0")
            type.qualifiedName == "long" -> DSTNumberLiteral(type, "0")
            type.qualifiedName == "float" -> DSTNumberLiteral(type, "0.0")
            type.qualifiedName == "double" -> DSTNumberLiteral(type, "0.0")
            else -> DSTNullLiteral(type)
        }
    }
}