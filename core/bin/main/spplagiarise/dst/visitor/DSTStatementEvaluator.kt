package spplagiarise.dst.visitor

import spplagiarise.dst.*

interface DSTStatementEvaluator<T, U> {
    abstract fun evaluateDSTSyntheticMethodCall(node: DSTSyntheticMethodCall, context: U): T
    abstract fun evaluateDSTBlockStatement(node: DSTBlockStatement, context: U): T
    abstract fun evaluateDSTExpressionStatement(node: DSTExpressionStatement, context: U): T
    abstract fun evaluateDSTLocalVariableDeclarationGroup(node: DSTLocalVariableDeclarationGroup, context: U): T
    abstract fun evaluateDSTLocalVariableDeclaration(node: DSTLocalVariableDeclaration, context: U): T
    abstract fun evaluateDSTSingleVariableDeclaration(node: DSTSingleVariableDeclaration, context: U): T
    abstract fun evaluateDSTWhileStatement(node: DSTWhileStatement, context: U): T
    abstract fun evaluateDSTDoWhileStatement(node: DSTDoWhileStatement, context: U): T
    abstract fun evaluateDSTForStatement(node: DSTForStatement, context: U): T
    abstract fun evaluateDSTForEachStatement(node: DSTForEachStatement, context: U): T
    abstract fun evaluateDSTSuperMethodCall(node: DSTSuperMethodCall, context: U): T
    abstract fun evaluateDSTSuperConstructorCall(node: DSTSuperConstructorCall, context: U): T
    abstract fun evaluateDSTThisConstructorCall(node: DSTThisConstructorCall, context: U): T
    abstract fun evaluateDSTSynchronisedStatement(node: DSTSynchronisedStatement, context: U): T
    abstract fun evaluateDSTLabeledStatement(node: DSTLabeledStatement, context: U): T
    abstract fun evaluateDSTBreakStatement(node: DSTBreakStatement, context: U): T
    abstract fun evaluateDSTContinueStatement(node: DSTContinueStatement, context: U): T
    abstract fun evaluateDSTReturnStatement(node: DSTReturnStatement, context: U): T
    abstract fun evaluateDSTThrowStatement(node: DSTThrowStatement, context: U): T
    abstract fun evaluateDSTIfStatement(node: DSTIfStatement, context: U): T
    abstract fun evaluateDSTSwitchStatement(node: DSTSwitchStatement, context: U): T
    abstract fun evaluateDSTSwitchCase(node: DSTSwitchCase, context: U): T
    abstract fun evaluateDSTTryStatement(node: DSTTryStatement, context: U): T
    abstract fun evaluateDSTEmptyStatement(node: DSTEmptyStatement, context: U): T
    abstract fun evaluateDSTTypeDeclarationStatement(node: DSTTypeDeclarationStatement, context: U): T
    abstract fun evaluateDSTAssertStatement(node: DSTAssertStatement, context: U): T
}

fun <T> DSTStatement.evaluate(evaluator: DSTStatementEvaluator<T, Unit>): T = this.evaluate(evaluator, Unit)

fun <T, U> DSTStatement.evaluate(evaluator: DSTStatementEvaluator<T, U>, context: U): T {
    return when (this) {
        is DSTSyntheticMethodCall -> evaluator.evaluateDSTSyntheticMethodCall(this, context)
        is DSTBlockStatement -> evaluator.evaluateDSTBlockStatement(this, context)
        is DSTExpressionStatement -> evaluator.evaluateDSTExpressionStatement(this, context)
        is DSTLocalVariableDeclarationGroup -> evaluator.evaluateDSTLocalVariableDeclarationGroup(this, context)
        is DSTLocalVariableDeclaration -> evaluator.evaluateDSTLocalVariableDeclaration(this, context)
        is DSTSingleVariableDeclaration -> evaluator.evaluateDSTSingleVariableDeclaration(this, context)
        is DSTWhileStatement -> evaluator.evaluateDSTWhileStatement(this, context)
        is DSTDoWhileStatement -> evaluator.evaluateDSTDoWhileStatement(this, context)
        is DSTForStatement -> evaluator.evaluateDSTForStatement(this, context)
        is DSTForEachStatement -> evaluator.evaluateDSTForEachStatement(this, context)
        is DSTSuperMethodCall -> evaluator.evaluateDSTSuperMethodCall(this, context)
        is DSTSuperConstructorCall -> evaluator.evaluateDSTSuperConstructorCall(this, context)
        is DSTThisConstructorCall -> evaluator.evaluateDSTThisConstructorCall(this, context)
        is DSTSynchronisedStatement -> evaluator.evaluateDSTSynchronisedStatement(this, context)
        is DSTLabeledStatement -> evaluator.evaluateDSTLabeledStatement(this, context)
        is DSTBreakStatement -> evaluator.evaluateDSTBreakStatement(this, context)
        is DSTContinueStatement -> evaluator.evaluateDSTContinueStatement(this, context)
        is DSTReturnStatement -> evaluator.evaluateDSTReturnStatement(this, context)
        is DSTThrowStatement -> evaluator.evaluateDSTThrowStatement(this, context)
        is DSTIfStatement -> evaluator.evaluateDSTIfStatement(this, context)
        is DSTSwitchStatement -> evaluator.evaluateDSTSwitchStatement(this, context)
        is DSTSwitchCase -> evaluator.evaluateDSTSwitchCase(this, context)
        is DSTTryStatement -> evaluator.evaluateDSTTryStatement(this, context)
        is DSTEmptyStatement -> evaluator.evaluateDSTEmptyStatement(this, context)
        is DSTTypeDeclarationStatement -> evaluator.evaluateDSTTypeDeclarationStatement(this, context)
        is DSTAssertStatement -> evaluator.evaluateDSTAssertStatement(this, context)
        else -> throw IllegalArgumentException("Unknown type ${this.javaClass}")
    }
}
