package spplagiarise.obfuscation.filters

import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.PrefixExpression
import spplagiarise.analytics.IAnalyticContext
import spplagiarise.ast.KnownTypeLibrary
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncDecExpanderFilter: DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var knownTypeLibrary: KnownTypeLibrary

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTPrefixExpression(node: DSTPrefixExpression) {
        if (node.parent == null || node.parent !is DSTExpressionStatement) return
        if (!config.extreme && !random.randomBoolean()) return
        when {
            node.operator == PrefixExpression.Operator.INCREMENT -> {
                val assignee = node.expression.clone()
                val expression = DSTSingleInfixExpression(node.typeBinding, InfixExpression.Operator.PLUS, node.expression, DSTNumberLiteral(knownTypeLibrary.intType, "1"))
                val assignment = DSTAssignment(node.typeBinding, Assignment.Operator.ASSIGN, assignee, expression)
                if (node.parent != null) {
                    node.parent!!.replace(node, assignment)
                    analyticContext.makeModification(this::class)
                }
            }

            node.operator == PrefixExpression.Operator.DECREMENT -> {
                val assignee = node.expression.clone()
                val expression = DSTSingleInfixExpression(node.typeBinding, InfixExpression.Operator.MINUS, node.expression, DSTNumberLiteral(knownTypeLibrary.intType, "1"))
                val assignment = DSTAssignment(node.typeBinding, Assignment.Operator.ASSIGN, assignee, expression)
                if (node.parent != null) {
                    node.parent!!.replace(node, assignment)
                    analyticContext.makeModification(this::class)
                }
            }

            node.operator == PrefixExpression.Operator.NOT -> {
                //  !(cond) -> cond == false
                val infix = DSTSingleInfixExpression(knownTypeLibrary.booleanType, InfixExpression.Operator.EQUALS, node.expression, DSTBooleanLiteral(knownTypeLibrary.booleanType, false))
                if (node.parent != null) {
                    node.parent!!.replace(node, infix)
                    analyticContext.makeModification(this::class)
                }
            }
        }
    }
}