package spplagiarise.obfuscation.l3

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.ast.KnownTypeLibrary
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.naming.DeferredNameMappingContext
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeclareRedundantConstants : DSTObfuscatorFilter {

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var knownTypes: KnownTypeLibrary

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var mappingContext: DeferredNameMappingContext
    
    @Inject
    private lateinit var config: Configuration

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        if (node.binding.declaringClass != null) return
        if (!config.extreme && !random.randomBoolean()) return

        val (name, value) = generateNameTypeAndValue()
        val typeName = value.typeBinding.name

        val field = DSTFieldGroup(
                if (random.randomBoolean()) AccessModifier.PUBLIC else AccessModifier.PACKAGEPROTECTED,
                FieldModifier(false, false, true, false, random.randomBoolean()),
                DSTSimpleType(DSTKnownSimpleName(typeName, value.typeBinding), value.typeBinding),
                listOf(
                        DSTFieldDeclaration(DSTKnownSimpleName(name, value.typeBinding), 0, value)
                )
        )

        val newBodyDeclarations = node.bodyDeclarations.toMutableList()
        newBodyDeclarations.add(0, field)
        node.bodyDeclarations = newBodyDeclarations
        analyticContext.makeModification(this::class)
    }

    override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
        if (node.body == null) return
        if (!config.extreme && !random.randomBoolean()) return

        val (name, value) = generateNameTypeAndValue()
        val typeName = value.typeBinding.name

        val local = DSTLocalVariableDeclarationGroup(
                listOf(DSTLocalVariableDeclaration(DSTKnownSimpleName(name, value.typeBinding), 0, value)),
                DSTSimpleType(DSTKnownSimpleName(typeName, value.typeBinding), value.typeBinding)
        )

        val newBodyStatements = node.body!!.statements.toMutableList()
        newBodyStatements.add(0, local)
        node.body!!.statements = newBodyStatements
        node.body!!.statements.forEach { it.parent = node.body }
        analyticContext.makeModification(this::class)
    }

    private fun generateNameTypeAndValue(): Pair<String, DSTLiteral> {
        val nameBase = random.randomIndex(constantNames)
        val name = mappingContext.generateRandomName(nameBase)

        val index = random.randomIndex(4)
        val literal = when (index) {
            0 -> DSTStringLiteral(knownTypes.stringType, "\"${random.randomString()}\"")
            1 -> DSTNumberLiteral(knownTypes.intType, random.randomInt().toString())
            else -> DSTNumberLiteral(knownTypes.doubleType, random.randomDouble().toString())
        }

        return Pair(name, literal)
    }

    private val constantNames = listOf(
            "max",
            "min",
            "limit",
            "upperLimit",
            "lowerLimit",
            "bound",
            "upperBound",
            "lowerBound",
            "count",
            "value",
            "width",
            "maxWidth",
            "minWidth",
            "key",
            "token",
            "name",
            "identifier",
            "numItems"
    )
}
