package spplagiarise.dst.visitor

import spplagiarise.dst.*

interface DSTExpressionOrNameEvaluator<T, U> {
    abstract fun evaluateDSTStringLiteral(node: DSTStringLiteral, context: U): T
    abstract fun evaluateDSTCharLiteral(node: DSTCharLiteral, context: U): T
    abstract fun evaluateDSTBooleanLiteral(node: DSTBooleanLiteral, context: U): T
    abstract fun evaluateDSTNumberLiteral(node: DSTNumberLiteral, context: U): T
    abstract fun evaluateDSTTypeLiteral(node: DSTTypeLiteral, context: U): T
    abstract fun evaluateDSTNullLiteral(node: DSTNullLiteral, context: U): T
    abstract fun evaluateDSTMethodCall(node: DSTMethodCall, context: U): T
    abstract fun evaluateDSTConstructorCall(node: DSTConstructorCall, context: U): T
    abstract fun evaluateDSTAssignment(node: DSTAssignment, context: U): T
    abstract fun evaluateDSTPrefixExpression(node: DSTPrefixExpression, context: U): T
    abstract fun evaluateDSTMultiInfixExpression(node: DSTMultiInfixExpression, context: U): T
    abstract fun evaluateDSTSingleInfixExpression(node: DSTSingleInfixExpression, context: U): T
    abstract fun evaluateDSTPostfixExpression(node: DSTPostfixExpression, context: U): T
    abstract fun evaluateDSTVariableDeclarationExpression(node: DSTVariableDeclarationExpression, context: U): T
    abstract fun evaluateDSTArrayAccess(node: DSTArrayAccess, context: U): T
    abstract fun evaluateDSTParenthesisedExpression(node: DSTParenthesisedExpression, context: U): T
    abstract fun evaluateDSTCastExpression(node: DSTCastExpression, context: U): T
    abstract fun evaluateDSTArrayCreation(node: DSTArrayCreation, context: U): T
    abstract fun evaluateDSTArrayInitialiser(node: DSTArrayInitialiser, context: U): T
    abstract fun evaluateDSTConditionalExpression(node: DSTConditionalExpression, context: U): T
    abstract fun evaluateDSTFieldAccessExpression(node: DSTFieldAccessExpression, context: U): T
    abstract fun evaluateDSTSuperFieldAccessExpression(node: DSTSuperFieldAccessExpression, context: U): T
    abstract fun evaluateDSTThisExpression(node: DSTThisExpression, context: U): T
    abstract fun evaluateDSTInstanceOfExpression(node: DSTInstanceOfExpression, context: U): T
    abstract fun evaluateDSTLambdaExpression(node: DSTLambdaExpression, context: U): T
    abstract fun evaluateDSTCreationRefExpression(node: DSTCreationRefExpression, context: U): T
    abstract fun evaluateDSTTypeMethodRefExpression(node: DSTTypeMethodRefExpression, context: U): T
    abstract fun evaluateDSTSuperMethodRefExpression(node: DSTSuperMethodRefExpression, context: U): T
    abstract fun evaluateDSTExpressionMethodRefExpression(node: DSTExpressionMethodRefExpression, context: U): T
    abstract fun evaluateDSTKnownSimpleName(node: DSTKnownSimpleName, context: U): T
    abstract fun evaluateDSTDeferredSimpleName(node: DSTDeferredSimpleName, context: U): T
    abstract fun evaluateDSTQualifiedName(node: DSTQualifiedName, context: U): T
}

fun <T> DSTExpressionOrName.evaluate(evaluator: DSTExpressionOrNameEvaluator<T, Unit>): T {
    return this.evaluate(evaluator, Unit)
}

fun <T, U> DSTExpressionOrName.evaluate(evaluator: DSTExpressionOrNameEvaluator<T, U>, context: U): T {
    return when (this) {
        is DSTStringLiteral -> evaluator.evaluateDSTStringLiteral(this, context)
        is DSTCharLiteral -> evaluator.evaluateDSTCharLiteral(this, context)
        is DSTBooleanLiteral -> evaluator.evaluateDSTBooleanLiteral(this, context)
        is DSTNumberLiteral -> evaluator.evaluateDSTNumberLiteral(this, context)
        is DSTTypeLiteral -> evaluator.evaluateDSTTypeLiteral(this, context)
        is DSTNullLiteral -> evaluator.evaluateDSTNullLiteral(this, context)
        is DSTMethodCall -> evaluator.evaluateDSTMethodCall(this, context)
        is DSTConstructorCall -> evaluator.evaluateDSTConstructorCall(this, context)
        is DSTAssignment -> evaluator.evaluateDSTAssignment(this, context)
        is DSTPrefixExpression -> evaluator.evaluateDSTPrefixExpression(this, context)
        is DSTMultiInfixExpression -> evaluator.evaluateDSTMultiInfixExpression(this, context)
        is DSTSingleInfixExpression -> evaluator.evaluateDSTSingleInfixExpression(this, context)
        is DSTPostfixExpression -> evaluator.evaluateDSTPostfixExpression(this, context)
        is DSTVariableDeclarationExpression -> evaluator.evaluateDSTVariableDeclarationExpression(this, context)
        is DSTArrayAccess -> evaluator.evaluateDSTArrayAccess(this, context)
        is DSTParenthesisedExpression -> evaluator.evaluateDSTParenthesisedExpression(this, context)
        is DSTCastExpression -> evaluator.evaluateDSTCastExpression(this, context)
        is DSTArrayCreation -> evaluator.evaluateDSTArrayCreation(this, context)
        is DSTArrayInitialiser -> evaluator.evaluateDSTArrayInitialiser(this, context)
        is DSTConditionalExpression -> evaluator.evaluateDSTConditionalExpression(this, context)
        is DSTFieldAccessExpression -> evaluator.evaluateDSTFieldAccessExpression(this, context)
        is DSTSuperFieldAccessExpression -> evaluator.evaluateDSTSuperFieldAccessExpression(this, context)
        is DSTThisExpression -> evaluator.evaluateDSTThisExpression(this, context)
        is DSTInstanceOfExpression -> evaluator.evaluateDSTInstanceOfExpression(this, context)
        is DSTLambdaExpression -> evaluator.evaluateDSTLambdaExpression(this, context)
        is DSTCreationRefExpression -> evaluator.evaluateDSTCreationRefExpression(this, context)
        is DSTTypeMethodRefExpression -> evaluator.evaluateDSTTypeMethodRefExpression(this, context)
        is DSTSuperMethodRefExpression -> evaluator.evaluateDSTSuperMethodRefExpression(this, context)
        is DSTExpressionMethodRefExpression -> evaluator.evaluateDSTExpressionMethodRefExpression(this, context)
        is DSTKnownSimpleName -> evaluator.evaluateDSTKnownSimpleName(this, context)
        is DSTDeferredSimpleName -> evaluator.evaluateDSTDeferredSimpleName(this, context)
        is DSTQualifiedName -> evaluator.evaluateDSTQualifiedName(this, context)
        else -> throw IllegalArgumentException("Unknown type ${this.javaClass}")
    }
}