package spplagiarise.obfuscation.l6

import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddBracketsToExpressionFilter: DSTObfuscatorFilter {

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTMultiInfixExpression(node: DSTMultiInfixExpression) {
        if (node.parent == null)
            return

        if (!config.extreme && !random.randomBoolean()) return

        val expr = DSTParenthesisedExpression(node.typeBinding, node.clone())
        node.parent!!.replace(node, expr)
    }

    override fun visitDSTAssignment(node: DSTAssignment) {
        if (!config.extreme && !random.randomBoolean()) return

        val expr = DSTParenthesisedExpression(node.typeBinding, node.expression)
        node.expression = expr
        node.expression.parent = node
    }
}