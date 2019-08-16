package spplagiarise.obfuscation.util

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.ast.KnownTypeLibrary
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.InfixExpression
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SwitchToIfFilter: DSTObfuscatorFilter {
    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var knownTypes: KnownTypeLibrary

    override fun visitDSTSwitchStatement(node: DSTSwitchStatement) {
        val expr = node.expression
        if (expr is DSTName && expr.binding is IVariableBinding && (expr.binding as IVariableBinding).type.isEnum)
            return

        val statements = node.statements
        val cases = node.statements.mapIndexed { index, dstStatement -> if (dstStatement is DSTSwitchCase) index to dstStatement else null }
                .filterNotNull()

        fun List<DSTStatement>.subListFrom(index: Int): List<DSTStatement> {
            val sublist = this.subList(index, this.size)
            val nextBreak = sublist.mapIndexed { index, dstStatement -> if (dstStatement is DSTBreakStatement) index to dstStatement else null }
                    .filterNotNull()
                    .firstOrNull()

            if (nextBreak != null) {
                return sublist.subList(0, nextBreak.first)
            }

            return sublist
        }

        var rootIfStatement: DSTIfStatement? = null
        var currentStatement: DSTIfStatement? = null

        for ((index, case) in cases) {
            if (rootIfStatement == null) {
                if (case.isDefault) {
                    return
                } else {
                    val condition = DSTSingleInfixExpression(knownTypes.booleanType, InfixExpression.Operator.EQUALS, case.expression!!, node.expression.clone())
                    val body = DSTBlockStatement(statements.subListFrom(index+1))
                    rootIfStatement = DSTIfStatement(condition, body, null)
                    currentStatement = rootIfStatement
                }
            } else {
                if (case.isDefault) {
                    currentStatement!!.thenStatement = DSTBlockStatement(statements.subListFrom(index+1))
                    break
                } else {
                    val condition = DSTSingleInfixExpression(knownTypes.booleanType, InfixExpression.Operator.EQUALS, case.expression!!, node.expression.clone())
                    val body = DSTBlockStatement(statements.subListFrom(index+1))
                    val ifStatement = DSTIfStatement(condition, body, null)
                    currentStatement!!.elseStatement = ifStatement
                    currentStatement = ifStatement
                }
            }
        }

        node.parent!!.replace(node, rootIfStatement!!)
        analyticContext.makeModification(this::class)
    }
}