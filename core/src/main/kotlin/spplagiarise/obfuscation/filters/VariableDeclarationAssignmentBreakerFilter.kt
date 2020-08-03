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
class VariableDeclarationAssignmentBreakerFilter : DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTBlockStatement(node: DSTBlockStatement) {
        if (!config.extreme && !random.randomBoolean()) return
        val statementsCopy = node.statements.toMutableList()

        val varDecls = node.statements.mapIndexedNotNull { index, dstStatement ->
            if (dstStatement is DSTLocalVariableDeclarationGroup && !dstStatement.type.binding.isArray)
                index to dstStatement
            else
                null
        }

        varDecls.reversed().forEach { (index, statement) ->
            statement.variables
                    .filter { it.initialiser != null }
                    .filter { it.initialiser !is DSTArrayInitialiser }
                    .forEach {

                        if (it.initialiser is DSTArrayInitialiser)
                            TODO()

                        val assignment = DSTAssignment(statement.type.binding, Assignment.Operator.ASSIGN, it.name.clone(), it.initialiser!!)
                        it.initialiser = null
                        statementsCopy.add(index + 1, DSTExpressionStatement(assignment))
                        analyticContext.makeModification(this::class)
                    }
        }

        node.statements = statementsCopy
        statementsCopy.forEach { it.parent = node }
    }
}