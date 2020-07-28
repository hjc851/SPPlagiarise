package spplagiarise.obfuscation.l5

import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.InfixExpression
import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.DSTAssignment
import spplagiarise.dst.DSTExpressionStatement
import spplagiarise.dst.DSTSingleInfixExpression
import spplagiarise.dst.clone
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CombinedAssignmentExpanderFilter: DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTAssignment(node: DSTAssignment) {
        if (node.parent == null || node.parent !is DSTExpressionStatement) return
        if (node.operator != Assignment.Operator.ASSIGN) {
            if (!config.extreme && !random.randomBoolean()) return
            val equivalentOp = equivalentBinaryOperatorFor(node.operator)

            if (equivalentOp != null) {
                val infix = DSTSingleInfixExpression(node.typeBinding, equivalentOp, node.assignee.clone(), node.expression)
                node.expression = infix
                infix.parent = node
                analyticContext.makeModification(this::class)
            }
        }
    }

    private fun equivalentBinaryOperatorFor(op: Assignment.Operator): InfixExpression.Operator? {
        return when (op) {
            Assignment.Operator.PLUS_ASSIGN -> InfixExpression.Operator.PLUS
            Assignment.Operator.MINUS_ASSIGN -> InfixExpression.Operator.MINUS
            Assignment.Operator.TIMES_ASSIGN -> InfixExpression.Operator.TIMES
            Assignment.Operator.DIVIDE_ASSIGN -> InfixExpression.Operator.DIVIDE
            Assignment.Operator.BIT_AND_ASSIGN -> InfixExpression.Operator.AND
            Assignment.Operator.BIT_OR_ASSIGN -> InfixExpression.Operator.OR
            Assignment.Operator.BIT_XOR_ASSIGN -> InfixExpression.Operator.XOR
            Assignment.Operator.REMAINDER_ASSIGN -> InfixExpression.Operator.REMAINDER
            Assignment.Operator.LEFT_SHIFT_ASSIGN -> InfixExpression.Operator.LEFT_SHIFT
            Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN -> InfixExpression.Operator.RIGHT_SHIFT_SIGNED
            Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN -> InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED
            else -> null
        }
    }
}