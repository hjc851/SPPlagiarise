package spplagiarise.obfuscation.l3

import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.IVariableBinding
import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandardiseVariableDeclarationsAtMethodStart : DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTBlockStatement(node: DSTBlockStatement) {
        if (!config.extreme && !random.randomBoolean()) return
        val statementsCopy = node.statements.toMutableList()
        val decls = mutableListOf<DSTLocalVariableDeclarationGroup>()

        val indicies = mutableListOf<Int>()
        node.statements.forEachIndexed { index, dstStatement ->
            if (dstStatement is DSTLocalVariableDeclarationGroup && !dstStatement.type.binding.isArray)
                indicies.add(index)
        }

        indicies.reversed().forEach { index ->
            val decl = statementsCopy.removeAt(index) as DSTLocalVariableDeclarationGroup

            // Insert the original assignments at the original position
            for (variable in decl.variables.reversed()) {
                val initialiser = variable.initialiser
                if (initialiser != null && initialiser !is DSTArrayInitialiser) {
                    val type = if (initialiser is DSTName) (initialiser.binding as IVariableBinding).type else if (initialiser is DSTExpression) initialiser.typeBinding else TODO()
                    val assignment = DSTAssignment(type, Assignment.Operator.ASSIGN, variable.name.clone(), variable.initialiser!!)
                    variable.initialiser = null
                    statementsCopy.add(index, DSTExpressionStatement(assignment))
                }
            }

            decls.add(decl)
            analyticContext.makeModification(this::class)
        }

        decls.forEach {
            statementsCopy.add(0, it)
        }

        node.statements = statementsCopy
        statementsCopy.forEach { it.parent = node }
    }

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        val declsCopy = node.bodyDeclarations.toMutableList()
        val decls = mutableListOf<DSTFieldGroup>()

        val indicies = mutableListOf<Int>()
        node.bodyDeclarations.forEachIndexed { index, dstBodyDeclaration ->
            if (dstBodyDeclaration is DSTFieldGroup && !dstBodyDeclaration.type.binding.isArray)
                indicies.add(index)
        }

        indicies.reversed().forEach {
            decls.add(declsCopy.removeAt(it) as DSTFieldGroup)
        }

        decls.reversed().forEach {
            declsCopy.add(0, it)
        }

        node.bodyDeclarations = declsCopy
    }
}