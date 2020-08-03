package spplagiarise.obfuscation.filters

import org.eclipse.jdt.core.dom.Assignment
import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoveKnownFieldAssignmentToInitialiserFilter : DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        val fieldGroups = node.bodyDeclarations.filterIsInstance<DSTFieldGroup>()

        val staticInitialisations = mutableListOf<DSTAssignment>()
        val instanceInitialisations = mutableListOf<DSTAssignment>()

        for (fieldGroup in fieldGroups) {
            for (field in fieldGroup.fields) {
                if (field.initialiser != null && !fieldGroup.modifiers.isFinal && !fieldGroup.type.binding.isArray) {
                    val assignment = DSTAssignment(fieldGroup.type.binding, Assignment.Operator.ASSIGN, field.name.clone(), field.initialiser!!)
                    field.initialiser = null

                    analyticContext.makeModification(this::class)

                    if (fieldGroup.modifiers.isStatic) {
                        staticInitialisations.add(assignment)
                    } else {
                        instanceInitialisations.add(assignment)
                    }
                }
            }
        }

        if (staticInitialisations.isNotEmpty()) {
            val staticBlock = getInitialiserBlock(node, true)

            val statements = staticBlock.body.statements + staticInitialisations.map { DSTExpressionStatement(it) }
            val body = DSTBlockStatement(statements)
            staticBlock.body = body
            body.parent = staticBlock
        }

        if (instanceInitialisations.isNotEmpty()) {
            val instanceBlock = getInitialiserBlock(node, false)

            val statements = instanceBlock.body.statements + instanceInitialisations.map { DSTExpressionStatement(it) }
            val body = DSTBlockStatement(statements)
            instanceBlock.body = body
        }
    }

    private fun getInitialiserBlock(node: DSTClassOrInterfaceTypeDeclaration, isStatic: Boolean): DSTInitialiser {
        var block = node.bodyDeclarations
                .firstOrNull { it is DSTInitialiser && it.isStatic == isStatic } as DSTInitialiser?

        if (block == null) {
            block = DSTInitialiser(isStatic, DSTBlockStatement(emptyList()))
            node.bodyDeclarations = node.bodyDeclarations + block
            block.parent = node
            analyticContext.makeModification(this::class)
        }

        return block
    }
}