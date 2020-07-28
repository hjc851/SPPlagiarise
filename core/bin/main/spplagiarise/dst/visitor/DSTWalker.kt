package spplagiarise.dst.visitor

import spplagiarise.dst.*

fun DSTNode.walk(visitor: DSTNodeVisitor) {
    val walker = DSTNodeWalker(visitor)
    this.accept(walker)
}

class DSTNodeWalker(val visitor: DSTNodeVisitor) : DSTNodeVisitor {

    private fun List<DSTNode>.accept(visitor: DSTNodeVisitor) = this.forEach { it.accept(visitor) }

    override fun visitDSTCompilationUnit(node: DSTCompilationUnit) {
        visitor.visitDSTCompilationUnit(node)

        node.packageDeclaration?.accept(this)
        node.imports.accept(this)
        node.types.accept(this)
    }

    override fun visitDSTPackageDeclaration(node: DSTPackageDeclaration) {
        visitor.visitDSTPackageDeclaration(node)

        node.name.accept(this)
    }

    override fun visitDSTSingleImportDeclaration(node: DSTSingleImportDeclaration) {
        visitor.visitDSTSingleImportDeclaration(node)

        node.name.accept(this)
    }

    override fun visitDSTPackageImportDeclaration(node: DSTPackageImportDeclaration) {
        visitor.visitDSTPackageImportDeclaration(node)

        node.name.accept(this)
    }

    override fun visitDSTStaticFieldImportDeclaration(node: DSTStaticFieldImportDeclaration) {
        visitor.visitDSTStaticFieldImportDeclaration(node)

        node.name.accept(this)
    }

    override fun visitDSTStaticMethodImportDeclaration(node: DSTStaticMethodImportDeclaration) {
        visitor.visitDSTStaticMethodImportDeclaration(node)

        node.name.accept(this)
    }

    override fun visitDSTStaticAsterixImportDeclaration(node: DSTStaticAsterixImportDeclaration) {
        visitor.visitDSTStaticAsterixImportDeclaration(node)

        node.name.accept(this)
    }

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        visitor.visitDSTClassOrInterfaceTypeDeclaration(node)

        node.name.accept(this)
        node.genericTypes.accept(this)
        node.extends?.accept(this)
        node.implements.accept(this)
        node.bodyDeclarations.accept(this)
    }

    override fun visitDSTEnumTypeDeclaration(node: DSTEnumTypeDeclaration) {
        visitor.visitDSTEnumTypeDeclaration(node)

        node.name.accept(this)
        node.implements.accept(this)
        node.enumConstants.accept(this)
        node.bodyDeclarations.accept(this)
    }

    override fun visitDSTAnnotationTypeDeclaration(node: DSTAnnotationTypeDeclaration) {
        visitor.visitDSTAnnotationTypeDeclaration(node)

        node.name.accept(this)
        node.bodyDeclarations.accept(this)
    }

    override fun visitDSTEnumConstant(node: DSTEnumConstant) {
        visitor.visitDSTEnumConstant(node)

        node.name.accept(this)
        node.arguments.accept(this)
        node.classBody?.accept(this)
    }

    override fun visitDSTAnnotationMember(node: DSTAnnotationMember) {
        visitor.visitDSTAnnotationMember(node)

        node.type.accept(this)
        node.name.accept(this)
        node.defaultValue?.accept(this)
    }

    override fun visitDSTFieldGroup(node: DSTFieldGroup) {
        visitor.visitDSTFieldGroup(node)

        node.type.accept(this)
        node.fields.accept(this)
    }

    override fun visitDSTFieldDeclaration(node: DSTFieldDeclaration) {
        visitor.visitDSTFieldDeclaration(node)

        node.name.accept(this)
        node.initialiser?.accept(this)
    }

    override fun visitDSTConstructorDeclaration(node: DSTConstructorDeclaration) {
        visitor.visitDSTConstructorDeclaration(node)

        node.typeParameters.accept(this)
        node.name.accept(this)
        node.parameters.accept(this)
        node.throws.accept(this)
        node.body?.accept(this)
    }

    override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
        visitor.visitDSTMethodDeclaration(node)

        node.returnType.accept(this)
        node.typeParameters.accept(this)
        node.name.accept(this)
        node.parameters.accept(this)
        node.throws.accept(this)
        node.body?.accept(this)
    }

    override fun visitDSTInitialiser(node: DSTInitialiser) {
        visitor.visitDSTInitialiser(node)

        node.body.accept(this)
    }

    override fun visitDSTKnownSimpleName(node: DSTKnownSimpleName) {
        visitor.visitDSTKnownSimpleName(node)
    }

    override fun visitDSTDeferredSimpleName(node: DSTDeferredSimpleName) {
        visitor.visitDSTDeferredSimpleName(node)
    }

    override fun visitDSTQualifiedName(node: DSTQualifiedName) {
        visitor.visitDSTQualifiedName(node)

        node.qualifier.accept(this)
        node.name.accept(this)
    }

    override fun visitDSTMethodCall(node: DSTMethodCall) {
        visitor.visitDSTMethodCall(node)

        node.expression?.accept(this)
        node.typeArguments.accept(this)
        node.name.accept(this)
        node.arguments.accept(this)
    }

    override fun visitDSTConstructorCall(node: DSTConstructorCall) {
        visitor.visitDSTConstructorCall(node)

        node.expression?.accept(this)
        node.typeArguments.accept(this)
        node.type.accept(this)
        node.arguments.accept(this)
        node.anonymousClassBody?.accept(this)
    }

    override fun visitDSTAssignment(node: DSTAssignment) {
        visitor.visitDSTAssignment(node)

        node.assignee.accept(this)
        node.expression.accept(this)
    }

    override fun visitDSTPrefixExpression(node: DSTPrefixExpression) {
        visitor.visitDSTPrefixExpression(node)

        node.expression.accept(this)
    }

    override fun visitDSTMultiInfixExpression(node: DSTMultiInfixExpression) {
        visitor.visitDSTMultiInfixExpression(node)

        node.expressions.accept(this)
    }

    override fun visitDSTSingleInfixExpression(node: DSTSingleInfixExpression) {
        visitor.visitDSTSingleInfixExpression(node)

        node.lhs.accept(this)
        node.rhs.accept(this)
    }

    override fun visitDSTPostfixExpression(node: DSTPostfixExpression) {
        visitor.visitDSTPostfixExpression(node)

        node.expression.accept(this)
    }

    override fun visitDSTVariableDeclarationExpression(node: DSTVariableDeclarationExpression) {
        visitor.visitDSTVariableDeclarationExpression(node)

        node.type.accept(this)
        node.variables.accept(this)
    }

    override fun visitDSTArrayAccess(node: DSTArrayAccess) {
        visitor.visitDSTArrayAccess(node)

        node.array.accept(this)
        node.index.accept(this)
    }

    override fun visitDSTParenthesisedExpression(node: DSTParenthesisedExpression) {
        visitor.visitDSTParenthesisedExpression(node)

        node.expr.accept(this)
    }

    override fun visitDSTCastExpression(node: DSTCastExpression) {
        visitor.visitDSTCastExpression(node)

        node.expression.accept(this)
        node.type.accept(this)
    }

    override fun visitDSTArrayCreation(node: DSTArrayCreation) {
        visitor.visitDSTArrayCreation(node)

        node.type.accept(this)
        node.dimensions.accept(this)
        node.initialiser?.accept(this)
    }

    override fun visitDSTArrayInitialiser(node: DSTArrayInitialiser) {
        visitor.visitDSTArrayInitialiser(node)

        node.expressions.accept(this)
    }

    override fun visitDSTConditionalExpression(node: DSTConditionalExpression) {
        visitor.visitDSTConditionalExpression(node)

        node.condition.accept(this)
        node.thenExpression.accept(this)
        node.elseExpression.accept(this)
    }

    override fun visitDSTFieldAccessExpression(node: DSTFieldAccessExpression) {
        visitor.visitDSTFieldAccessExpression(node)

        node.scope?.accept(this)
        node.name.accept(this)
    }

    override fun visitDSTSuperFieldAccessExpression(node: DSTSuperFieldAccessExpression) {
        visitor.visitDSTSuperFieldAccessExpression(node)

        node.qualifier?.accept(this)
        node.name.accept(this)
    }

    override fun visitDSTThisExpression(node: DSTThisExpression) {
        visitor.visitDSTThisExpression(node)

        node.qualifier?.accept(this)
    }

    override fun visitDSTInstanceOfExpression(node: DSTInstanceOfExpression) {
        visitor.visitDSTInstanceOfExpression(node)

        node.expression.accept(this)
        node.type.accept(this)
    }

    override fun visitDSTLambdaExpression(node: DSTLambdaExpression) {
        visitor.visitDSTLambdaExpression(node)

        node.parameters.accept(this)
        node.body.accept(this)
    }

    override fun visitDSTCreationRefExpression(node: DSTCreationRefExpression) {
        visitor.visitDSTCreationRefExpression(node)

        node.type.accept(this)
        node.typeParameters.accept(this)
    }

    override fun visitDSTTypeMethodRefExpression(node: DSTTypeMethodRefExpression) {
        visitor.visitDSTTypeMethodRefExpression(node)

        node.type.accept(this)
        node.typeParameters.accept(this)
        node.name.accept(this)
    }

    override fun visitDSTSuperMethodRefExpression(node: DSTSuperMethodRefExpression) {
        visitor.visitDSTSuperMethodRefExpression(node)

        node.qualifier?.accept(this)
        node.typeParameters.accept(this)
        node.name.accept(this)
    }

    override fun visitDSTExpressionMethodRefExpression(node: DSTExpressionMethodRefExpression) {
        visitor.visitDSTExpressionMethodRefExpression(node)

        node.expression.accept(this)
        node.typeParameters.accept(this)
        node.name.accept(this)
    }

    override fun visitDSTStringLiteral(node: DSTStringLiteral) {
        visitor.visitDSTStringLiteral(node)
    }

    override fun visitDSTCharLiteral(node: DSTCharLiteral) {
        visitor.visitDSTCharLiteral(node)
    }

    override fun visitDSTBooleanLiteral(node: DSTBooleanLiteral) {
        visitor.visitDSTBooleanLiteral(node)
    }

    override fun visitDSTNumberLiteral(node: DSTNumberLiteral) {
        visitor.visitDSTNumberLiteral(node)
    }

    override fun visitDSTTypeLiteral(node: DSTTypeLiteral) {
        visitor.visitDSTTypeLiteral(node)

        node.type.accept(this)
    }

    override fun visitDSTNullLiteral(node: DSTNullLiteral) {
        visitor.visitDSTNullLiteral(node)
    }

    override fun visitDSTAnonymousClassBody(node: DSTAnonymousClassBody) {
        visitor.visitDSTAnonymousClassBody(node)

        node.declarations.accept(this)
    }

    override fun visitDSTSimpleType(node: DSTSimpleType) {
        visitor.visitDSTSimpleType(node)

        node.name.accept(this)
    }

    override fun visitDSTArrayType(node: DSTArrayType) {
        visitor.visitDSTArrayType(node)

        node.typeName.accept(this)
    }

    override fun visitDSTParameterisedType(node: DSTParameterisedType) {
        visitor.visitDSTParameterisedType(node)

        node.baseType.accept(this)
        node.typeParameters.accept(this)
    }

    override fun visitDSTQualifiedType(node: DSTQualifiedType) {
        visitor.visitDSTQualifiedType(node)

        node.qualifier.accept(this)
        node.name.accept(this)
    }

    override fun visitDSTNameQualifiedType(node: DSTNameQualifiedType) {
        visitor.visitDSTNameQualifiedType(node)

        node.qualifier.accept(this)
        node.name.accept(this)
    }

    override fun visitDSTWildcardType(node: DSTWildcardType) {
        visitor.visitDSTWildcardType(node)
    }

    override fun visitDSTUnionType(node: DSTUnionType) {
        visitor.visitDSTUnionType(node)

        node.types.accept(this)
    }

    override fun visitDSTIntersectionType(node: DSTIntersectionType) {
        visitor.visitDSTIntersectionType(node)

        node.types.accept(this)
    }

    override fun visitDSTTypeParameter(node: DSTTypeParameter) {
        visitor.visitDSTTypeParameter(node)

        node.name.accept(this)
        node.bounds.accept(this)
    }

    override fun visitDSTParameter(node: DSTParameter) {
        visitor.visitDSTParameter(node)

        node.type.accept(this)
        node.name.accept(this)
    }

    override fun visitDSTDimension(node: DSTDimension) {
        visitor.visitDSTDimension(node)

        TODO()
    }

    override fun visitDSTModifier(node: DSTModifier) {
        visitor.visitDSTModifier(node)
    }

    override fun visitDSTBlockStatement(node: DSTBlockStatement) {
        visitor.visitDSTBlockStatement(node)

        node.statements.accept(this)
    }

    override fun visitDSTExpressionStatement(node: DSTExpressionStatement) {
        visitor.visitDSTExpressionStatement(node)

        node.expression.accept(this)
    }

    override fun visitDSTLocalVariableDeclarationGroup(node: DSTLocalVariableDeclarationGroup) {
        visitor.visitDSTLocalVariableDeclarationGroup(node)

        node.type.accept(this)
        node.variables.accept(this)
    }

    override fun visitDSTLocalVariableDeclaration(node: DSTLocalVariableDeclaration) {
        visitor.visitDSTLocalVariableDeclaration(node)

        node.name.accept(this)
        node.initialiser?.accept(this)
    }

    override fun visitDSTSingleVariableDeclaration(node: DSTSingleVariableDeclaration) {
        visitor.visitDSTSingleVariableDeclaration(node)

        node.type.accept(this)
        node.name.accept(this)
        node.initialiser?.accept(this)
    }

    override fun visitDSTWhileStatement(node: DSTWhileStatement) {
        visitor.visitDSTWhileStatement(node)

        node.condition.accept(this)
        node.body.accept(this)
    }

    override fun visitDSTDoWhileStatement(node: DSTDoWhileStatement) {
        visitor.visitDSTDoWhileStatement(node)

        node.body.accept(this)
        node.condition.accept(this)
    }

    override fun visitDSTForStatement(node: DSTForStatement) {
        visitor.visitDSTForStatement(node)

        node.initialisers.accept(this)
        node.condition?.accept(this)
        node.updaters.accept(this)
        node.body.accept(this)
    }

    override fun visitDSTForEachStatement(node: DSTForEachStatement) {
        visitor.visitDSTForEachStatement(node)

        node.variable.accept(this)
        node.collection.accept(this)
        node.body.accept(this)
    }

    override fun visitDSTSuperMethodCall(node: DSTSuperMethodCall) {
        visitor.visitDSTSuperMethodCall(node)

        node.qualifier?.accept(this)
        node.typeArguments.accept(this)
        node.name.accept(this)
        node.arguments.accept(this)
    }

    override fun visitDSTSuperConstructorCall(node: DSTSuperConstructorCall) {
        visitor.visitDSTSuperConstructorCall(node)

        node.qualifier?.accept(this)
        node.typeArguments.accept(this)
        node.arguments.accept(this)
    }

    override fun visitDSTThisConstructorCall(node: DSTThisConstructorCall) {
        visitor.visitDSTThisConstructorCall(node)

        node.typeArguments.accept(this)
        node.arguments.accept(this)
    }

    override fun visitDSTSynchronisedStatement(node: DSTSynchronisedStatement) {
        visitor.visitDSTSynchronisedStatement(node)

        node.monitor.accept(this)
        node.body.accept(this)
    }

    override fun visitDSTLabeledStatement(node: DSTLabeledStatement) {
        visitor.visitDSTLabeledStatement(node)

        node.statement.accept(this)
    }

    override fun visitDSTBreakStatement(node: DSTBreakStatement) {
        visitor.visitDSTBreakStatement(node)
    }

    override fun visitDSTContinueStatement(node: DSTContinueStatement) {
        visitor.visitDSTContinueStatement(node)
    }

    override fun visitDSTReturnStatement(node: DSTReturnStatement) {
        visitor.visitDSTReturnStatement(node)

        node.expression?.accept(this)
    }

    override fun visitDSTThrowStatement(node: DSTThrowStatement) {
        visitor.visitDSTThrowStatement(node)

        node.exception.accept(this)
    }

    override fun visitDSTIfStatement(node: DSTIfStatement) {
        visitor.visitDSTIfStatement(node)

        node.condition.accept(this)
        node.thenStatement.accept(this)
        node.elseStatement?.accept(this)
    }

    override fun visitDSTSwitchStatement(node: DSTSwitchStatement) {
        visitor.visitDSTSwitchStatement(node)

        node.expression.accept(this)
        node.statements.accept(this)
    }

    override fun visitDSTSwitchCase(node: DSTSwitchCase) {
        visitor.visitDSTSwitchCase(node)

        node.expression?.accept(this)
    }

    override fun visitDSTTryStatement(node: DSTTryStatement) {
        visitor.visitDSTTryStatement(node)

        node.resources.accept(this)
        node.body.accept(this)
        node.catchClauses.accept(this)
        node.finally?.accept(this)
    }

    override fun visitDSTEmptyStatement(node: DSTEmptyStatement) {
        visitor.visitDSTEmptyStatement(node)
    }

    override fun visitDSTTypeDeclarationStatement(node: DSTTypeDeclarationStatement) {
        visitor.visitDSTTypeDeclarationStatement(node)

        node.type.accept(this)
    }

    override fun visitDSTAssertStatement(node: DSTAssertStatement) {
        visitor.visitDSTAssertStatement(node)

        node.expression.accept(this)
        node.message.accept(this)
    }

    override fun visitDSTCatchClause(node: DSTCatchClause) {
        visitor.visitDSTCatchClause(node)

        node.exception.accept(this)
        node.body.accept(this)
    }

    override fun visitDSTSyntheticMethodCall(node: DSTSyntheticMethodCall) {
        visitor.visitDSTSyntheticMethodCall(node)

        node.parameters.accept(this)
    }

    override fun visitDSTSyntheticMethodDeclaration(node: DSTSyntheticMethodDeclaration) {
        visitor.visitDSTSyntheticMethodDeclaration(node)

        node.parameters.accept(this)
        node.throws.accept(this)
        node.body.accept(this)
    }

    override fun visitDSTSyntheticField(dstSyntheticField: DSTSyntheticField) {
        visitor.visitDSTSyntheticField(dstSyntheticField)

        dstSyntheticField.name.accept(this)
        dstSyntheticField.type.accept(this)
        dstSyntheticField.value.accept(this)
    }

    override fun visitDSTSyntheticName(dstSyntheticName: DSTSyntheticName) {
        visitor.visitDSTSyntheticName(dstSyntheticName)
    }
}


