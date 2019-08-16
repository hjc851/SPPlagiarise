package spplagiarise.dst.visitor

import spplagiarise.dst.*

interface DSTNodeEvaluator<T, U> {
    abstract fun evaluateDSTKnownSimpleName(node: DSTKnownSimpleName, context: U): T
    abstract fun evaluateDSTDeferredSimpleName(node: DSTDeferredSimpleName, context: U): T
    abstract fun evaluateDSTQualifiedName(node: DSTQualifiedName, context: U): T
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
    abstract fun evaluateDSTSuperFieldAcessExpression(node: DSTSuperFieldAccessExpression, context: U): T
    abstract fun evaluateDSTThisExpression(node: DSTThisExpression, context: U): T
    abstract fun evaluateDSTInstanceOfExpression(node: DSTInstanceOfExpression, context: U): T
    abstract fun evaluateDSTLambdaExpression(node: DSTLambdaExpression, context: U): T
    abstract fun evaluateDSTCreationRefExpression(node: DSTCreationRefExpression, context: U): T
    abstract fun evaluateDSTTypeMethodRefExpression(node: DSTTypeMethodRefExpression, context: U): T
    abstract fun evaluateDSTSuperMethodRefExpression(node: DSTSuperMethodRefExpression, context: U): T
    abstract fun evaluateDSTExpressionMethodRefExpression(node: DSTExpressionMethodRefExpression, context: U): T
    abstract fun evaluateDSTStringLiteral(node: DSTStringLiteral, context: U): T
    abstract fun evaluateDSTCharLiteral(node: DSTCharLiteral, context: U): T
    abstract fun evaluateDSTBooleanLiteral(node: DSTBooleanLiteral, context: U): T
    abstract fun evaluateDSTNumberLiteral(node: DSTNumberLiteral, context: U): T
    abstract fun evaluateDSTTypeLiteral(node: DSTTypeLiteral, context: U): T
    abstract fun evaluateDSTNullLiteral(node: DSTNullLiteral, context: U): T
    abstract fun evaluateDSTAnonymousClassBody(node: DSTAnonymousClassBody, context: U): T
    abstract fun evaluateDSTSimpleType(node: DSTSimpleType, context: U): T
    abstract fun evaluateDSTArrayType(node: DSTArrayType, context: U): T
    abstract fun evaluateDSTParameterisedType(node: DSTParameterisedType, context: U): T
    abstract fun evaluateDSTQualifiedType(node: DSTQualifiedType, context: U): T
    abstract fun evaluateDSTNameQualifiedType(node: DSTNameQualifiedType, context: U): T
    abstract fun evaluateDSTWildcardType(node: DSTWildcardType, context: U): T
    abstract fun evaluateDSTUnionType(node: DSTUnionType, context: U): T
    abstract fun evaluateDSTIntersectionType(node: DSTIntersectionType, context: U): T
    abstract fun evaluateDSTTypeParameter(node: DSTTypeParameter, context: U): T
    abstract fun evaluateDSTCompilationUnit(node: DSTCompilationUnit, context: U): T
    abstract fun evaluateDSTPackageDeclaration(node: DSTPackageDeclaration, context: U): T
    abstract fun evaluateDSTSingleImportDeclaration(node: DSTSingleImportDeclaration, context: U): T
    abstract fun evaluateDSTPackageImportDeclaration(node: DSTPackageImportDeclaration, context: U): T
    abstract fun evaluateDSTStaticFieldImportDeclaration(node: DSTStaticFieldImportDeclaration, context: U): T
    abstract fun evaluateDSTStaticMethodImportDeclaration(node: DSTStaticMethodImportDeclaration, context: U): T
    abstract fun evaluateDSTStaticAsterixImportDeclaration(node: DSTStaticAsterixImportDeclaration, context: U): T
    abstract fun evaluateDSTFieldDeclaration(node: DSTFieldDeclaration, context: U): T
    abstract fun evaluateDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration, context: U): T
    abstract fun evaluateDSTEnumTypeDeclaration(node: DSTEnumTypeDeclaration, context: U): T
    abstract fun evaluateDSTAnnotationTypeDeclaration(node: DSTAnnotationTypeDeclaration, context: U): T
    abstract fun evaluateDSTEnumConstant(node: DSTEnumConstant, context: U): T
    abstract fun evaluateDSTAnnotationMember(node: DSTAnnotationMember, context: U): T
    abstract fun evaluateDSTFieldGroup(node: DSTFieldGroup, context: U): T
    abstract fun evaluateDSTConstructorDeclaration(node: DSTConstructorDeclaration, context: U): T
    abstract fun evaluateDSTMethodDeclaration(node: DSTMethodDeclaration, context: U): T
    abstract fun evaluateDSTInitialiser(node: DSTInitialiser, context: U): T
    abstract fun evaluateDSTParameter(node: DSTParameter, context: U): T
    abstract fun evaluateDSTDimension(node: DSTDimension, context: U): T
    abstract fun evaluateDSTModifier(node: DSTModifier, context: U): T
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
    abstract fun evaluateDSTCatchClause(node: DSTCatchClause, context: U): T
}

fun <T> DSTNode.evaluate(evaluator: DSTNodeEvaluator<T, Unit>): T {
    return this.evaluate(evaluator)
}

fun <T, U> DSTNode.evaluate(evaluator: DSTNodeEvaluator<T, U>, context: U): T {
    return when (this) {
        is DSTKnownSimpleName -> evaluator.evaluateDSTKnownSimpleName(this, context)
        is DSTDeferredSimpleName -> evaluator.evaluateDSTDeferredSimpleName(this, context)
        is DSTQualifiedName -> evaluator.evaluateDSTQualifiedName(this, context)
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
        is DSTSuperFieldAccessExpression -> evaluator.evaluateDSTSuperFieldAcessExpression(this, context)
        is DSTThisExpression -> evaluator.evaluateDSTThisExpression(this, context)
        is DSTInstanceOfExpression -> evaluator.evaluateDSTInstanceOfExpression(this, context)
        is DSTLambdaExpression -> evaluator.evaluateDSTLambdaExpression(this, context)
        is DSTCreationRefExpression -> evaluator.evaluateDSTCreationRefExpression(this, context)
        is DSTTypeMethodRefExpression -> evaluator.evaluateDSTTypeMethodRefExpression(this, context)
        is DSTSuperMethodRefExpression -> evaluator.evaluateDSTSuperMethodRefExpression(this, context)
        is DSTExpressionMethodRefExpression -> evaluator.evaluateDSTExpressionMethodRefExpression(this, context)
        is DSTStringLiteral -> evaluator.evaluateDSTStringLiteral(this, context)
        is DSTCharLiteral -> evaluator.evaluateDSTCharLiteral(this, context)
        is DSTBooleanLiteral -> evaluator.evaluateDSTBooleanLiteral(this, context)
        is DSTNumberLiteral -> evaluator.evaluateDSTNumberLiteral(this, context)
        is DSTTypeLiteral -> evaluator.evaluateDSTTypeLiteral(this, context)
        is DSTNullLiteral -> evaluator.evaluateDSTNullLiteral(this, context)
        is DSTAnonymousClassBody -> evaluator.evaluateDSTAnonymousClassBody(this, context)
        is DSTSimpleType -> evaluator.evaluateDSTSimpleType(this, context)
        is DSTArrayType -> evaluator.evaluateDSTArrayType(this, context)
        is DSTParameterisedType -> evaluator.evaluateDSTParameterisedType(this, context)
        is DSTQualifiedType -> evaluator.evaluateDSTQualifiedType(this, context)
        is DSTNameQualifiedType -> evaluator.evaluateDSTNameQualifiedType(this, context)
        is DSTWildcardType -> evaluator.evaluateDSTWildcardType(this, context)
        is DSTUnionType -> evaluator.evaluateDSTUnionType(this, context)
        is DSTIntersectionType -> evaluator.evaluateDSTIntersectionType(this, context)
        is DSTTypeParameter -> evaluator.evaluateDSTTypeParameter(this, context)
        is DSTCompilationUnit -> evaluator.evaluateDSTCompilationUnit(this, context)
        is DSTPackageDeclaration -> evaluator.evaluateDSTPackageDeclaration(this, context)
        is DSTSingleImportDeclaration -> evaluator.evaluateDSTSingleImportDeclaration(this, context)
        is DSTPackageImportDeclaration -> evaluator.evaluateDSTPackageImportDeclaration(this, context)
        is DSTStaticFieldImportDeclaration -> evaluator.evaluateDSTStaticFieldImportDeclaration(this, context)
        is DSTStaticMethodImportDeclaration -> evaluator.evaluateDSTStaticMethodImportDeclaration(this, context)
        is DSTStaticAsterixImportDeclaration -> evaluator.evaluateDSTStaticAsterixImportDeclaration(this, context)
        is DSTFieldDeclaration -> evaluator.evaluateDSTFieldDeclaration(this, context)
        is DSTClassOrInterfaceTypeDeclaration -> evaluator.evaluateDSTClassOrInterfaceTypeDeclaration(this, context)
        is DSTEnumTypeDeclaration -> evaluator.evaluateDSTEnumTypeDeclaration(this, context)
        is DSTAnnotationTypeDeclaration -> evaluator.evaluateDSTAnnotationTypeDeclaration(this, context)
        is DSTEnumConstant -> evaluator.evaluateDSTEnumConstant(this, context)
        is DSTAnnotationMember -> evaluator.evaluateDSTAnnotationMember(this, context)
        is DSTFieldGroup -> evaluator.evaluateDSTFieldGroup(this, context)
        is DSTConstructorDeclaration -> evaluator.evaluateDSTConstructorDeclaration(this, context)
        is DSTMethodDeclaration -> evaluator.evaluateDSTMethodDeclaration(this, context)
        is DSTInitialiser -> evaluator.evaluateDSTInitialiser(this, context)
        is DSTParameter -> evaluator.evaluateDSTParameter(this, context)
        is DSTDimension -> evaluator.evaluateDSTDimension(this, context)
        is DSTModifier -> evaluator.evaluateDSTModifier(this, context)
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
        is DSTCatchClause -> evaluator.evaluateDSTCatchClause(this, context)
        else -> throw IllegalArgumentException("Unknown name ${this.javaClass}")
    }
}