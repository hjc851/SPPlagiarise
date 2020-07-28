package spplagiarise.obfuscation.util

import org.eclipse.jdt.core.dom.IVariableBinding
import spplagiarise.dst.*
import spplagiarise.dst.visitor.DSTExpressionOrNameEvaluator
import spplagiarise.dst.visitor.DSTStatementEvaluator
import spplagiarise.dst.visitor.evaluate

//  Finds left-most binding of a dot-separated expression, as well as all other child expressions
//  e.g. in x.getY().z, finds x (all others are derived)
//  e.g. in x.y.z, finds x
//  e.g. in x.getY(a).z, finds x and a (both are left-most of dot-separated expressions
object ExpressionBindingResolver : DSTExpressionOrNameEvaluator<List<DSTSimpleName>, Unit>, DSTStatementEvaluator<List<DSTSimpleName>, Unit> {

    //
    //  Statements
    //

    override fun evaluateDSTSyntheticMethodCall(node: DSTSyntheticMethodCall, context: Unit): List<DSTSimpleName> {
        return node.parameters.flatMap { it.evaluate(this) }
    }

    override fun evaluateDSTBlockStatement(node: DSTBlockStatement, context: Unit): List<DSTSimpleName> {
        return node.statements.flatMap { it.evaluate(this) }
    }

    override fun evaluateDSTExpressionStatement(node: DSTExpressionStatement, context: Unit): List<DSTSimpleName> {
        return node.expression.evaluate(this)
    }

    override fun evaluateDSTLocalVariableDeclarationGroup(node: DSTLocalVariableDeclarationGroup, context: Unit): List<DSTSimpleName> {
        return node.variables.flatMap { it.evaluate(this) }
    }

    override fun evaluateDSTLocalVariableDeclaration(node: DSTLocalVariableDeclaration, context: Unit): List<DSTSimpleName> {
        return node.initialiser?.evaluate(this) ?: emptyList()
    }

    override fun evaluateDSTSingleVariableDeclaration(node: DSTSingleVariableDeclaration, context: Unit): List<DSTSimpleName> {
        return node.initialiser?.evaluate(this) ?: emptyList()
    }

    override fun evaluateDSTWhileStatement(node: DSTWhileStatement, context: Unit): List<DSTSimpleName> {
        val cond = node.condition.evaluate(this)
        val body = node.body.evaluate(this)
        return cond + body
    }

    override fun evaluateDSTDoWhileStatement(node: DSTDoWhileStatement, context: Unit): List<DSTSimpleName> {
        val cond = node.condition.evaluate(this)
        val body = node.body.evaluate(this)
        return cond + body
    }

    override fun evaluateDSTForStatement(node: DSTForStatement, context: Unit): List<DSTSimpleName> {
        val init = node.initialisers.flatMap { it.evaluate(this) }
        val cond = node.condition?.evaluate(this) ?: emptyList()
        val update = node.updaters.flatMap { it.evaluate(this) }
        val body = node.body.evaluate(this)
        return init + cond + update + body
    }

    override fun evaluateDSTForEachStatement(node: DSTForEachStatement, context: Unit): List<DSTSimpleName> {
        val collection = node.collection.evaluate(this)
        val body = node.body.evaluate(this)
        return collection + body
    }

    override fun evaluateDSTSuperMethodCall(node: DSTSuperMethodCall, context: Unit): List<DSTSimpleName> {
        val qualfier = node.qualifier?.evaluate(this) ?: emptyList()
        val args = node.arguments.flatMap { it.evaluate(this) }
        return qualfier + args
    }

    override fun evaluateDSTSuperConstructorCall(node: DSTSuperConstructorCall, context: Unit): List<DSTSimpleName> {
        return node.arguments.flatMap { it.evaluate(this) }
    }

    override fun evaluateDSTThisConstructorCall(node: DSTThisConstructorCall, context: Unit): List<DSTSimpleName> {
        return node.arguments.flatMap { it.evaluate(this) }
    }

    override fun evaluateDSTSynchronisedStatement(node: DSTSynchronisedStatement, context: Unit): List<DSTSimpleName> {
        return node.monitor.evaluate(this) + node.body.evaluate(this)
    }

    override fun evaluateDSTLabeledStatement(node: DSTLabeledStatement, context: Unit): List<DSTSimpleName> {
        return node.statement.evaluate(this)
    }

    override fun evaluateDSTBreakStatement(node: DSTBreakStatement, context: Unit): List<DSTSimpleName> {
        return emptyList()
    }

    override fun evaluateDSTContinueStatement(node: DSTContinueStatement, context: Unit): List<DSTSimpleName> {
        return emptyList()
    }

    override fun evaluateDSTReturnStatement(node: DSTReturnStatement, context: Unit): List<DSTSimpleName> {
        return node.expression?.evaluate(this) ?: emptyList()
    }

    override fun evaluateDSTThrowStatement(node: DSTThrowStatement, context: Unit): List<DSTSimpleName> {
        return node.exception.evaluate(this)
    }

    override fun evaluateDSTIfStatement(node: DSTIfStatement, context: Unit): List<DSTSimpleName> {
        val condition = node.condition.evaluate(this)
        val then = node.thenStatement.evaluate(this)
        val remainder = node.elseStatement?.evaluate(this) ?: emptyList()

        return condition + then + remainder
    }

    override fun evaluateDSTSwitchStatement(node: DSTSwitchStatement, context: Unit): List<DSTSimpleName> {
        return node.expression.evaluate(this) + node.statements.flatMap { it.evaluate(this) }
    }

    override fun evaluateDSTSwitchCase(node: DSTSwitchCase, context: Unit): List<DSTSimpleName> {
        return emptyList()
    }

    override fun evaluateDSTTryStatement(node: DSTTryStatement, context: Unit): List<DSTSimpleName> {
        val resources = node.resources.flatMap { it.evaluate(this) }
        val body = node.body.evaluate(this)
        val catches = node.catchClauses.flatMap { it.body.evaluate(this) }
        val finally = node.finally?.evaluate(this) ?: emptyList()

        return resources + body + catches + finally
    }

    override fun evaluateDSTEmptyStatement(node: DSTEmptyStatement, context: Unit): List<DSTSimpleName> {
        return emptyList()
    }

    override fun evaluateDSTTypeDeclarationStatement(node: DSTTypeDeclarationStatement, context: Unit): List<DSTSimpleName> {
        return emptyList()
    }

    override fun evaluateDSTAssertStatement(node: DSTAssertStatement, context: Unit): List<DSTSimpleName> {
        return node.expression.evaluate(this) + node.message.evaluate(this)
    }

    //
    //  Expression or Name
    //

    override fun evaluateDSTStringLiteral(node: DSTStringLiteral, context: Unit): List<DSTSimpleName> = emptyList()
    override fun evaluateDSTCharLiteral(node: DSTCharLiteral, context: Unit): List<DSTSimpleName> = emptyList()
    override fun evaluateDSTBooleanLiteral(node: DSTBooleanLiteral, context: Unit): List<DSTSimpleName> = emptyList()
    override fun evaluateDSTNumberLiteral(node: DSTNumberLiteral, context: Unit): List<DSTSimpleName> = emptyList()
    override fun evaluateDSTTypeLiteral(node: DSTTypeLiteral, context: Unit): List<DSTSimpleName> = emptyList()
    override fun evaluateDSTNullLiteral(node: DSTNullLiteral, context: Unit): List<DSTSimpleName> = emptyList()

    override fun evaluateDSTMethodCall(node: DSTMethodCall, context: Unit): List<DSTSimpleName> {
        val scope = node.expression?.evaluate(this)

        val args = node.arguments.flatMap { it.evaluate(this) }

        if (scope != null)
            return scope + args

        return args
    }

    override fun evaluateDSTConstructorCall(node: DSTConstructorCall, context: Unit): List<DSTSimpleName> {
        val scope = node.expression?.evaluate(this)

        val args = node.arguments.flatMap { it.evaluate(this) }

        if (scope != null)
            return scope + args

        return args
    }

    override fun evaluateDSTAssignment(node: DSTAssignment, context: Unit): List<DSTSimpleName> = node.assignee.evaluate(this) + node.expression.evaluate(this)

    override fun evaluateDSTPrefixExpression(node: DSTPrefixExpression, context: Unit): List<DSTSimpleName> = node.expression.evaluate(this)
    override fun evaluateDSTMultiInfixExpression(node: DSTMultiInfixExpression, context: Unit): List<DSTSimpleName> = node.expressions.flatMap { it.evaluate(this) }
    override fun evaluateDSTSingleInfixExpression(node: DSTSingleInfixExpression, context: Unit): List<DSTSimpleName> = node.lhs.evaluate(this) + node.rhs.evaluate(this)
    override fun evaluateDSTPostfixExpression(node: DSTPostfixExpression, context: Unit): List<DSTSimpleName> = node.expression.evaluate(this)

    override fun evaluateDSTVariableDeclarationExpression(node: DSTVariableDeclarationExpression, context: Unit): List<DSTSimpleName> {
        return node.variables.mapNotNull { it.initialiser?.evaluate(this) }.flatten() + node.variables.map { it.name }
    }

    override fun evaluateDSTArrayAccess(node: DSTArrayAccess, context: Unit): List<DSTSimpleName> {
        return node.array.evaluate(this) + node.index.evaluate(this)
    }

    override fun evaluateDSTParenthesisedExpression(node: DSTParenthesisedExpression, context: Unit): List<DSTSimpleName> = node.expr.evaluate(this)

    override fun evaluateDSTCastExpression(node: DSTCastExpression, context: Unit): List<DSTSimpleName> {
        val expression = node.expression
        if (expression is DSTExpressionOrName) {
            return expression.evaluate(this)
        } else if (expression is DSTSuperMethodCall) {
            TODO()
        } else {
            TODO()
        }
    }

    override fun evaluateDSTArrayCreation(node: DSTArrayCreation, context: Unit): List<DSTSimpleName> = node.initialiser?.evaluate(this)
            ?: emptyList()

    override fun evaluateDSTArrayInitialiser(node: DSTArrayInitialiser, context: Unit): List<DSTSimpleName> = node.expressions.flatMap { it.evaluate(this) }

    override fun evaluateDSTConditionalExpression(node: DSTConditionalExpression, context: Unit): List<DSTSimpleName> {
        return node.condition.evaluate(this) + node.thenExpression.evaluate(this) + node.elseExpression.evaluate(this)
    }

    override fun evaluateDSTFieldAccessExpression(node: DSTFieldAccessExpression, context: Unit): List<DSTSimpleName> {
        if (node.scope == null) {
            return listOf(node.name)
        } else {
            return node.scope!!.evaluate(this)
        }
    }

    override fun evaluateDSTSuperFieldAccessExpression(node: DSTSuperFieldAccessExpression, context: Unit): List<DSTSimpleName> {
        return listOf(node.name)
    }

    override fun evaluateDSTThisExpression(node: DSTThisExpression, context: Unit): List<DSTSimpleName> = emptyList()
    override fun evaluateDSTInstanceOfExpression(node: DSTInstanceOfExpression, context: Unit): List<DSTSimpleName> = node.expression.evaluate(this)

    override fun evaluateDSTLambdaExpression(node: DSTLambdaExpression, context: Unit): List<DSTSimpleName> {
        return emptyList()
    }

    override fun evaluateDSTCreationRefExpression(node: DSTCreationRefExpression, context: Unit): List<DSTSimpleName> = emptyList()
    override fun evaluateDSTTypeMethodRefExpression(node: DSTTypeMethodRefExpression, context: Unit): List<DSTSimpleName> = emptyList()
    override fun evaluateDSTSuperMethodRefExpression(node: DSTSuperMethodRefExpression, context: Unit): List<DSTSimpleName> = emptyList()

    override fun evaluateDSTExpressionMethodRefExpression(node: DSTExpressionMethodRefExpression, context: Unit): List<DSTSimpleName> = node.expression.evaluate(this)

    override fun evaluateDSTKnownSimpleName(node: DSTKnownSimpleName, context: Unit): List<DSTSimpleName> {
        val binding = node.binding
        return if (binding is IVariableBinding)
            listOf(node)
        else
            emptyList()
    }

    override fun evaluateDSTDeferredSimpleName(node: DSTDeferredSimpleName, context: Unit): List<DSTSimpleName> {
        val binding = node.binding
        return if (binding is IVariableBinding)
            listOf(node)
        else
            emptyList()
    }

    override fun evaluateDSTQualifiedName(node: DSTQualifiedName, context: Unit): List<DSTSimpleName> {
        val binding = node.name.binding
        if (binding is IVariableBinding) {
            return node.qualifier.evaluate(this)
        }

        return emptyList()
    }
}