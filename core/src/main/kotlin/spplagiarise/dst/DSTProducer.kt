package spplagiarise.dst

import org.eclipse.jdt.core.dom.*
import spplagiarise.ast.ASTEvaluator
import spplagiarise.ast.evaluate
import spplagiarise.naming.DeferredNameContext
import spplagiarise.parser.BindingStore

class DSTProducer(
        val bindings: BindingStore,
        val nameContext: DeferredNameContext
) : ASTEvaluator<DSTNode>() {

    //  Top-level stuff

    override fun evaluateCompilationUnit(node: CompilationUnit): DSTCompilationUnit {
        val packageDeclaration = node.`package`?.evaluate(this) as DSTPackageDeclaration?
        val imports = node.imports().map { (it as ASTNode).evaluate(this) as DSTImportDeclaration }
        val types = node.types().map { (it as ASTNode).evaluate(this) as DSTTypeDeclaration }

        return DSTCompilationUnit(packageDeclaration, imports.toMutableList(), types.toMutableList())
    }

    override fun evaluatePackageDeclaration(node: PackageDeclaration): DSTPackageDeclaration {
        val name = nameContext.resolvePackageNameForBinding(node.resolveBinding())
        return DSTPackageDeclaration(name)
    }

    override fun evaluateImportDeclaration(node: ImportDeclaration): DSTImportDeclaration {
        return when {
            node.isStatic -> evaluateStaticImport(node)
            else -> evaluateNormalImport(node)
        }
    }

    private fun evaluateStaticImport(node: ImportDeclaration): DSTImportDeclaration {
        val binding = node.name.resolveBinding()

        when {
            node.isOnDemand -> {
                binding as ITypeBinding

                if (bindings.contains(binding.key)) {
                    val name = nameContext.resolveTypeNameForBinding(binding)
                    return DSTStaticAsterixImportDeclaration(name, binding)
                } else {
                    val name = node.name.evaluate(this) as DSTName
                    return DSTStaticAsterixImportDeclaration(name, binding)
                }
            }

            else -> {
                when (binding) {
                    is IMethodBinding -> {
                        val name = if (bindings.contains(binding.key))
                            nameContext.resolveMethodNameForBinding(binding)
                        else
                            node.name.evaluate(this)

                        return DSTStaticMethodImportDeclaration(name as DSTName, binding)
                    }

                    is IVariableBinding -> {
                        val name = if (bindings.contains(binding.key))
                            nameContext.resolveVariableNameForBinding(binding)
                        else
                            node.name.evaluate(this) as DSTName

                        return DSTStaticFieldImportDeclaration(name, binding)
                    }

                    else -> throw IllegalArgumentException("Unknown static import")
                }
            }
        }
    }

    private fun evaluateNormalImport(node: ImportDeclaration): DSTImportDeclaration {
        val binding = node.name.resolveBinding()

        when {
            node.isOnDemand -> {
                try {
                    binding as IPackageBinding

                    val name = if (bindings.contains(binding.key))
                        nameContext.resolvePackageNameForBinding(binding)
                    else
                        node.name.evaluate(this) as DSTName

                    return DSTPackageImportDeclaration(name, binding)
                } catch (e: Exception) {
                    throw e
                }
            }

            else -> {
                val name = node.name.evaluate(this) as DSTName
                return DSTSingleImportDeclaration(name, node.resolveBinding() as ITypeBinding)
            }
        }
    }

    //  Types

    override fun evaluateTypeDeclaration(node: TypeDeclaration): DSTClassOrInterfaceTypeDeclaration {
        val binding = node.resolveBinding()

        val accessModifier = AccessModifierFactory.get(node.modifiers)
        val isAbstract = Modifier.isAbstract(node.modifiers)
        val isStatic = Modifier.isStatic(node.modifiers)
        val declType = if (node.isInterface) TypeDeclarationType.INTERFACE else TypeDeclarationType.CLASS
        val name = nameContext.resolveTypeNameForBinding(binding)
        val parent = node.superclassType?.evaluate(this) as DSTType?
        val implements = node.superInterfaceTypes().map { (it as ASTNode).evaluate(this) as DSTType }

        val genericTypes = node.typeParameters().map { (it as ASTNode).evaluate(this) as DSTTypeParameter }
        val bodyDeclarations = node.bodyDeclarations().map { (it as ASTNode).evaluate(this) as DSTBodyDeclaration }

        return DSTClassOrInterfaceTypeDeclaration(accessModifier, isAbstract, isStatic, declType, name, genericTypes, parent, implements, bodyDeclarations, node.resolveBinding())
    }

    override fun evaluateAnnotationTypeDeclaration(node: AnnotationTypeDeclaration): DSTAnnotationTypeDeclaration {
        val accessModifier = AccessModifierFactory.get(node.modifiers)
        val name = nameContext.resolveTypeNameForBinding(node.resolveBinding())
        val bodyDeclarations = node.bodyDeclarations().map { (it as ASTNode).evaluate(this) as DSTAnnotationMember }

        return DSTAnnotationTypeDeclaration(accessModifier, name, bodyDeclarations, node.resolveBinding())
    }

    override fun evaluateAnnotationTypeMemberDeclaration(node: AnnotationTypeMemberDeclaration): DSTAnnotationMember {
        val accessModifier = AccessModifierFactory.get(node.modifiers)
        val name = node.name.evaluate(this) as DSTDeferredSimpleName
        val type = node.type.evaluate(this) as DSTType
        val defaultValue = node.default?.evaluate(this) as DSTExpressionOrName?

        return DSTAnnotationMember(accessModifier, name, type, defaultValue)
    }

    override fun evaluateEnumDeclaration(node: EnumDeclaration): DSTEnumTypeDeclaration {
        val accessModifier = AccessModifierFactory.get(node.modifiers)
        val name = nameContext.resolveTypeNameForBinding(node.resolveBinding())
        val implements = node.superInterfaceTypes().map { (it as ASTNode).evaluate(this) as DSTType }
        val enumConstants = node.enumConstants().map { (it as ASTNode).evaluate(this) as DSTEnumConstant }
        val bodyDeclarations = node.bodyDeclarations().map { (it as ASTNode).evaluate(this) as DSTBodyDeclaration }

        return DSTEnumTypeDeclaration(accessModifier, name, implements, enumConstants, bodyDeclarations, node.resolveBinding())
    }

    override fun evaluateEnumConstantDeclaration(node: EnumConstantDeclaration): DSTEnumConstant {
        val varBinding = node.resolveVariable()

        val name = nameContext.resolveVariableNameForBinding(varBinding)
        val arguments = node.arguments().map { (it as ASTNode).evaluate(this) as DSTExpressionOrName }
        val classBody = node.anonymousClassDeclaration?.let { evaluateAnonymousClassDeclaration(it) as DSTAnonymousClassBody }

        return DSTEnumConstant(name, arguments, classBody)
    }

    //  Members

    override fun evaluateFieldDeclaration(node: FieldDeclaration): DSTFieldGroup {
        val accessModifier = AccessModifierFactory.get(node.modifiers)
        val modifiers = FieldModifierFactory.get(node.modifiers)
        val type = node.type.evaluate(this) as DSTType
        val fields = node.fragments().map {
            (it as VariableDeclarationFragment)

            return@map DSTFieldDeclaration(
                    nameContext.resolveVariableNameForBinding(it.resolveBinding()),
                    it.extraDimensions,
                    it.initializer?.evaluate(this) as DSTExpressionOrName?
            )
        }
        return DSTFieldGroup(accessModifier, modifiers, type, fields)
    }

    override fun evaluateMethodDeclaration(node: MethodDeclaration): DSTBodyDeclaration {

        val binding = node.resolveBinding()

        val accessModifier = AccessModifierFactory.get(node.modifiers)
        val modifiers = MethodModifierFactory.get(node.modifiers)
        modifiers.isAbstract = Modifier.isAbstract(node.modifiers) || node.body == null

        val typeParameters = node.typeParameters().map { (it as ASTNode).evaluate(this) as DSTType }
        val parameters = node.parameters().map { (it as ASTNode).evaluate(this) as DSTParameter }
        val throws = node.thrownExceptionTypes().map { (it as ASTNode).evaluate(this) as DSTType }

        val body = node.body?.evaluate(this) as DSTBlockStatement?

        if (node.isConstructor) {
            val name = nameContext.resolveTypeNameForBinding(node.resolveBinding().declaringClass)

            return DSTConstructorDeclaration(
                    accessModifier,
                    modifiers,
                    typeParameters,
                    name,
                    parameters,
                    throws,
                    body
            )
        } else {
            val returnType = node.returnType2.evaluate(this) as DSTType
            val name = if (bindings.contains(binding.key)) {
                nameContext.resolveMethodNameForBinding(binding)
            } else {
                node.name.evaluate(this) as DSTSimpleName
            }

            return DSTMethodDeclaration(
                    accessModifier,
                    modifiers,
                    returnType,
                    typeParameters,
                    name,
                    parameters,
                    throws,
                    body,
                    node.resolveBinding()
            )
        }
    }

    //  Annotations

    override fun evaluateMarkerAnnotation(node: MarkerAnnotation): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateNormalAnnotation(node: NormalAnnotation): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateSingleMemberAnnotation(node: SingleMemberAnnotation): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateMemberValuePair(node: MemberValuePair): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //  Names & Types

    override fun evaluateSimpleName(node: SimpleName): DSTSimpleName {
        var binding = node.resolveBinding()

        if (binding is ITypeBinding)
            binding = binding.erasure
        else if (binding is IVariableBinding)
            binding = binding.variableDeclaration
        else if (binding is IMethodBinding)
            binding = binding.methodDeclaration

        try {
            val name = if (bindings.contains(binding.key))
                nameContext.resolveNameForBinding(binding)
            else
                DSTKnownSimpleName(node.identifier, binding)

            return name
        } catch (e: Exception) {
            throw e
        }
    }

    override fun evaluateQualifiedName(node: QualifiedName): DSTQualifiedName {
        var binding = node.resolveBinding()

        if (binding is ITypeBinding)
            binding = binding.erasure
        else if (binding is IVariableBinding)
            binding = binding.variableDeclaration
        else if (binding is IMethodBinding)
            binding = binding.methodDeclaration

        val qualifier = node.qualifier.evaluate(this) as DSTName
        val name = node.name.evaluate(this) as DSTSimpleName

        return DSTQualifiedName(qualifier, name, binding)
    }

    override fun evaluateArrayType(node: ArrayType): DSTArrayType {
        val baseType = node.elementType.evaluate(this) as DSTType
        val dimensions = node.dimensions

        return DSTArrayType(baseType, dimensions, node.resolveBinding())
    }

    override fun evaluateParameterizedType(node: ParameterizedType): DSTNode {

        if (node.resolveBinding().typeArguments.isNotEmpty() || node.resolveBinding().typeParameters.isNotEmpty())
            Unit


        val baseType = node.type.evaluate(this) as DSTType
        val typeParameters = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }
        return DSTParameterisedType(baseType, typeParameters, node.resolveBinding())
    }

    override fun evaluateNameQualifiedType(node: NameQualifiedType): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateQualifiedType(node: QualifiedType): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluatePrimitiveType(node: PrimitiveType): DSTSimpleType {
        return DSTSimpleType(DSTKnownSimpleName(node.primitiveTypeCode.toString(), node.resolveBinding()), node.resolveBinding())
    }

    override fun evaluateWildcardType(node: WildcardType): DSTNode {
        val bound = node.bound?.evaluate(this) as DSTType?
        return DSTWildcardType(bound, node.isUpperBound, node.resolveBinding())
    }

    override fun evaluateSimpleType(node: SimpleType): DSTSimpleType {
        val typeName = node.name.evaluate(this) as DSTName
        return DSTSimpleType(typeName, node.resolveBinding())
    }

    override fun evaluateUnionType(node: UnionType): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateIntersectionType(node: IntersectionType): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateTypeParameter(node: TypeParameter): DSTNode {
        val name = node.name.evaluate(this) as DSTName
        val bounds = node.typeBounds().map { (it as ASTNode).evaluate(this) as DSTType }

        return DSTTypeParameter(name, bounds, node.resolveBinding())
    }

    //

    override fun evaluateDimension(node: Dimension): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateModifier(node: Modifier): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateInitializer(node: Initializer): DSTInitialiser {
        val body = node.body.evaluate(this) as DSTBlockStatement
        return DSTInitialiser(Modifier.isStatic(node.modifiers), body)
    }

    override fun evaluateContinueStatement(node: ContinueStatement): DSTContinueStatement {
        return DSTContinueStatement(node.label?.evaluate(this) as DSTName?)
    }

    override fun evaluateExpressionStatement(node: ExpressionStatement): DSTStatement {
            val expr = node.expression.evaluate(this)

            if (expr is DSTExpression)
                return DSTExpressionStatement(expr)

            return expr as DSTStatement
    }

    override fun evaluateLabeledStatement(node: LabeledStatement): DSTLabeledStatement {
        return DSTLabeledStatement(node.label.identifier, node.body.evaluate(this) as DSTStatement)
    }

    override fun evaluateBlock(node: Block): DSTBlockStatement {
        val statements = node.statements().map { (it as ASTNode).evaluate(this) as DSTStatement }
        return DSTBlockStatement(statements)
    }

    override fun evaluateSynchronizedStatement(node: SynchronizedStatement): DSTSynchronisedStatement {
        val monitor = node.expression.evaluate(this) as DSTExpressionOrName
        val body = node.body.evaluate(this) as DSTStatement
        return DSTSynchronisedStatement(monitor, body)
    }

    override fun evaluateTryStatement(node: TryStatement): DSTTryStatement {
        val resources = node.resources().map { (it as ASTNode).evaluate(this) as DSTExpression }
        val body = node.body.evaluate(this) as DSTStatement
        val catchClause = node.catchClauses().map { evaluateCatchClause(it as CatchClause) }
        val finally = node.finally?.evaluate(this) as DSTStatement?

        return DSTTryStatement(resources, body, catchClause, finally)
    }

    override fun evaluateCatchClause(node: CatchClause): DSTCatchClause {
        val exception = evaluateSingleVariableDeclaration(node.exception) as DSTSingleVariableDeclaration
        val body = evaluateBlock(node.body)

        return DSTCatchClause(exception, body)
    }

    override fun evaluateAssertStatement(node: AssertStatement): DSTAssertStatement {
        val message = node.message.evaluate(this) as DSTExpressionOrName
        val expression = node.expression.evaluate(this) as DSTExpressionOrName

        return DSTAssertStatement(message, expression)
    }

    override fun evaluateThrowStatement(node: ThrowStatement): DSTThrowStatement {
        val expression = node.expression.evaluate(this) as DSTExpressionOrName
        return DSTThrowStatement(expression)
    }

    override fun evaluateTypeDeclarationStatement(node: TypeDeclarationStatement): DSTTypeDeclarationStatement {
        val declaration = node.declaration.evaluate(this) as DSTTypeDeclaration
        return DSTTypeDeclarationStatement(declaration)
    }

    override fun evaluateEmptyStatement(node: EmptyStatement): DSTEmptyStatement {
        return DSTEmptyStatement()
    }

    override fun evaluateReturnStatement(node: ReturnStatement): DSTReturnStatement {
        val expression = node.expression?.evaluate(this) as DSTExpressionOrName?
        return DSTReturnStatement(expression)
    }

    override fun evaluateBreakStatement(node: BreakStatement): DSTBreakStatement {
        return DSTBreakStatement(node.label?.evaluate(this) as DSTName?)
    }

    override fun evaluateInstanceofExpression(node: InstanceofExpression): DSTInstanceOfExpression {
        val expression = node.leftOperand.evaluate(this) as DSTExpressionOrName
        val type = node.rightOperand.evaluate(this) as DSTType

        return DSTInstanceOfExpression(node.resolveTypeBinding(), expression, type)
    }

    override fun evaluateArrayInitializer(node: ArrayInitializer): DSTArrayInitialiser {
        val expressions = node.expressions().map { (it as ASTNode).evaluate(this) as DSTExpressionOrName }
        return DSTArrayInitialiser(node.resolveTypeBinding(), expressions)
    }

    override fun evaluateArrayCreation(node: ArrayCreation): DSTArrayCreation {
        val type = evaluateArrayType(node.type)
        val dimensions = node.dimensions().map { (it as ASTNode).evaluate(this) as DSTExpressionOrName }
        val initialiser = node.initializer?.evaluate(this) as DSTArrayInitialiser?
        return DSTArrayCreation(node.resolveTypeBinding(), type, dimensions, initialiser)
    }

    override fun evaluateParenthesizedExpression(node: ParenthesizedExpression): DSTParenthesisedExpression {
        return DSTParenthesisedExpression(node.resolveTypeBinding(), node.expression.evaluate(this) as DSTExpressionOrName)
    }

    override fun evaluateCastExpression(node: CastExpression): DSTCastExpression {
        val expression = node.expression.evaluate(this) as DSTExpressionOrName
        val type = node.type.evaluate(this) as DSTType

        return DSTCastExpression(node.resolveTypeBinding(), expression, type)
    }

    //  Lambdas

    override fun evaluateLambdaExpression(node: LambdaExpression): DSTLambdaExpression {
        val parameters = node.parameters().map { (it as ASTNode).evaluate(this) }
        val body = node.body.evaluate(this) as DSTNode

        return DSTLambdaExpression(
                node.resolveTypeBinding(),
                parameters,
                body
        )
    }

    override fun evaluateCreationReference(node: CreationReference): DSTCreationRefExpression {
        val type = node.type.evaluate(this) as DSTType
        val typeParameters = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }
        return DSTCreationRefExpression(node.resolveTypeBinding(), type, typeParameters)
    }

    override fun evaluateTypeMethodReference(node: TypeMethodReference): DSTTypeMethodRefExpression {
        val type = node.type.evaluate(this) as DSTType
        val name = node.name.evaluate(this) as DSTSimpleName
        val typeParameters = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }

        return DSTTypeMethodRefExpression(node.resolveTypeBinding(), type, typeParameters, name)
    }

    override fun evaluateSuperMethodReference(node: SuperMethodReference): DSTSuperMethodRefExpression {
        val qualifier = node.qualifier?.evaluate(this) as DSTName?
        val name = node.name.evaluate(this) as DSTSimpleName
        val typeParameters = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }


        return DSTSuperMethodRefExpression(node.resolveTypeBinding(), qualifier, typeParameters, name)
    }

    override fun evaluateExpressionMethodReference(node: ExpressionMethodReference): DSTExpressionMethodRefExpression {
        val expression = node.expression.evaluate(this) as DSTExpressionOrName
        val name = node.name.evaluate(this) as DSTSimpleName
        val typeParameters = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }


        return DSTExpressionMethodRefExpression(node.resolveTypeBinding(), expression, typeParameters, name)
    }

    // Fields & Methods

    override fun evaluateArrayAccess(node: ArrayAccess): DSTArrayAccess {
        val array = node.array.evaluate(this) as DSTExpressionOrName
        val index = node.index.evaluate(this) as DSTExpressionOrName

        return DSTArrayAccess(node.resolveTypeBinding(), array, index)
    }

    override fun evaluateThisExpression(node: ThisExpression): DSTThisExpression {
        val qualifier = node.qualifier?.evaluate(this) as DSTName?
        return DSTThisExpression(node.resolveTypeBinding(), qualifier)
    }

    override fun evaluateSuperFieldAccess(node: SuperFieldAccess): DSTSuperFieldAccessExpression {
        val qualifier = node.qualifier?.evaluate(this) as DSTName?
        val name = node.name.evaluate(this) as DSTSimpleName

        return DSTSuperFieldAccessExpression(
                node.resolveTypeBinding(),
                qualifier,
                name
        )
    }

    override fun evaluateMethodInvocation(node: MethodInvocation): DSTMethodCall {
        try {
            val name = node.name.evaluate(this) as DSTName
            val arguments = node.arguments().map { (it as ASTNode).evaluate(this) as DSTExpressionOrName }
            val typeArguments = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }
            val expression = node.expression?.evaluate(this) as DSTExpressionOrName?

            return DSTMethodCall(
                    node.resolveMethodBinding().returnType,
                    name,
                    arguments,
                    typeArguments,
                    expression,
                    node.resolveMethodBinding()
            )
        } catch (e: Exception) {
            val binding = node.resolveMethodBinding()
            throw e
        }
    }

    override fun evaluateClassInstanceCreation(node: ClassInstanceCreation): DSTConstructorCall {
        val type = node.type.evaluate(this) as DSTType
        val arguments = node.arguments().map { (it as ASTNode).evaluate(this) as DSTExpressionOrName }
        val typeArguments = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }

        val anonymousClassBody = node.anonymousClassDeclaration?.evaluate(this) as DSTAnonymousClassBody?
        val expression = node.expression?.evaluate(this) as DSTExpressionOrName?

        return DSTConstructorCall(
                node.resolveTypeBinding(),
                node.resolveConstructorBinding(),
                type,
                arguments,
                typeArguments,
                anonymousClassBody,
                expression
        )
    }

    override fun evaluateAnonymousClassDeclaration(node: AnonymousClassDeclaration): DSTAnonymousClassBody {
        val type = node.resolveBinding()
        val body = node.bodyDeclarations().map { (it as ASTNode).evaluate(this) as DSTBodyDeclaration }

        return DSTAnonymousClassBody(type, body)
    }

    override fun evaluateConstructorInvocation(node: ConstructorInvocation): DSTThisConstructorCall {
        val arguments = node.arguments().map { (it as ASTNode).evaluate(this) as DSTExpressionOrName }
        val typeArguments = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }

        return DSTThisConstructorCall(node.resolveConstructorBinding(), arguments, typeArguments)
    }

    override fun evaluateSuperConstructorInvocation(node: SuperConstructorInvocation): DSTSuperConstructorCall {
        val qualifier = node.expression?.evaluate(this) as DSTExpressionOrName?
        val arguments = node.arguments().map { (it as ASTNode).evaluate(this) as DSTExpressionOrName }
        val typeArguments = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }

        return DSTSuperConstructorCall(node.resolveConstructorBinding(), qualifier, arguments, typeArguments)
    }

    override fun evaluateFieldAccess(node: FieldAccess): DSTFieldAccessExpression {
        val scope = node.expression?.let { (it as ASTNode).evaluate(this) as DSTExpressionOrName }
        val name = node.name.evaluate(this) as DSTSimpleName

        if (node.resolveFieldBinding().isEnumConstant)
            Unit

        return DSTFieldAccessExpression(node.resolveTypeBinding(), scope, name)
    }

    override fun evaluateSuperMethodInvocation(node: SuperMethodInvocation): DSTSuperMethodCall {
        val name = node.name.evaluate(this) as DSTName
        val qualifier = node.qualifier?.evaluate(this) as? DSTName
        val arguments = node.arguments().map { (it as ASTNode).evaluate(this) as DSTExpressionOrName }
        val typeArguments = node.typeArguments().map { (it as ASTNode).evaluate(this) as DSTType }

        return DSTSuperMethodCall(
                node.resolveMethodBinding(),
                name,
                qualifier,
                arguments,
                typeArguments
        )
    }

    //  Variable Declarations

    override fun evaluateSingleVariableDeclaration(node: SingleVariableDeclaration): DSTNode {
        val binding = node.resolveBinding()

        val type = node.type.evaluate(this) as DSTType
        val name = nameContext.resolveVariableNameForBinding(binding)
        val initialiser = node.initializer?.evaluate(this) as DSTExpressionOrName?

        return if (binding.isParameter) {
            DSTParameter(name, type, node.isVarargs)
        } else {
            DSTSingleVariableDeclaration(type, name, node.extraDimensions, initialiser)
        }
    }

    override fun evaluateVariableDeclarationFragment(node: VariableDeclarationFragment): DSTNode {
        val binding = node.resolveBinding()

        if (binding.isField)
            TODO()

        if (binding.isParameter)
            TODO()

        if (binding.isEnumConstant)
            TODO()

        // Assuming that it is a local variable
        val name = nameContext.resolveVariableNameForBinding(binding)
        val initialiser = node.initializer?.evaluate(this) as DSTExpressionOrName?

        return DSTLocalVariableDeclaration(name, node.extraDimensions, initialiser)
    }

    override fun evaluateVariableDeclarationStatement(node: VariableDeclarationStatement): DSTLocalVariableDeclarationGroup {
        val type = node.type.evaluate(this) as DSTType
        val locals = node.fragments().map { (it as ASTNode).evaluate(this) as DSTLocalVariableDeclaration }
        return DSTLocalVariableDeclarationGroup(locals, type)
    }

    override fun evaluateVariableDeclarationExpression(node: VariableDeclarationExpression): DSTVariableDeclarationExpression {
        val type = node.type.evaluate(this) as DSTType
        val variables = node.fragments().map { (it as ASTNode).evaluate(this) as DSTLocalVariableDeclaration }

        return DSTVariableDeclarationExpression(node.resolveTypeBinding(), type, variables)
    }

    //  Branching

    override fun evaluateIfStatement(node: IfStatement): DSTIfStatement {
        val condition = node.expression.evaluate(this) as DSTExpressionOrName
        val thenStatement = node.thenStatement.evaluate(this) as DSTStatement
        val elseStatement = node.elseStatement?.evaluate(this) as DSTStatement?

        return DSTIfStatement(condition, thenStatement, elseStatement)
    }

    override fun evaluateSwitchStatement(node: SwitchStatement): DSTSwitchStatement {
        val expr = node.expression.evaluate(this) as DSTExpressionOrName
        val statements = node.statements().map { (it as ASTNode).evaluate(this) as DSTStatement }
        return DSTSwitchStatement(expr, statements)
    }

    override fun evaluateSwitchCase(node: SwitchCase): DSTSwitchCase {
        val expr = node.expression?.evaluate(this) as DSTExpressionOrName?
        val isDefault = node.isDefault
        return DSTSwitchCase(expr, isDefault)
    }

    override fun evaluateConditionalExpression(node: ConditionalExpression): DSTConditionalExpression {
        val condition = node.expression.evaluate(this) as DSTExpressionOrName
        val thenExpr = node.thenExpression.evaluate(this) as DSTExpressionOrName
        val elseExpr = node.elseExpression.evaluate(this) as DSTExpressionOrName

        return DSTConditionalExpression(node.resolveTypeBinding(), condition, thenExpr, elseExpr)
    }

    //  Loops

    override fun evaluateForStatement(node: ForStatement): DSTForStatement {
        val initialisers = node.initializers().map { (it as ASTNode).evaluate(this) as DSTExpression }
        val condition = node.expression?.evaluate(this) as DSTExpressionOrName?
        val updaters = node.updaters().map { (it as ASTNode).evaluate(this) as DSTExpression }
        val body = node.body.evaluate(this) as DSTStatement

        return DSTForStatement(initialisers, condition, updaters, body)
    }

    override fun evaluateEnhancedForStatement(node: EnhancedForStatement): DSTForEachStatement {
        val variable = evaluateSingleVariableDeclaration(node.parameter) as DSTSingleVariableDeclaration
        val collection = node.expression.evaluate(this) as DSTExpressionOrName
        val body = node.body.evaluate(this) as DSTStatement

        return DSTForEachStatement(variable, collection, body)
    }

    override fun evaluateWhileStatement(node: WhileStatement): DSTWhileStatement {
        val condition = node.expression.evaluate(this) as DSTExpressionOrName
        val body = node.body.evaluate(this) as DSTStatement

        return DSTWhileStatement(condition, body)
    }

    override fun evaluateDoStatement(node: DoStatement): DSTDoWhileStatement {
        val condition = node.expression.evaluate(this) as DSTExpressionOrName
        val body = node.body.evaluate(this) as DSTStatement

        return DSTDoWhileStatement(condition, body)
    }

    //  Operators

    override fun evaluateAssignment(node: Assignment): DSTAssignment {
        val assignee = node.leftHandSide.evaluate(this) as DSTExpressionOrName
        val expression = node.rightHandSide.evaluate(this) as DSTExpressionOrName
        return DSTAssignment(node.resolveTypeBinding(), node.operator, assignee, expression)
    }

    override fun evaluatePrefixExpression(node: PrefixExpression): DSTPrefixExpression {
        val expression = node.operand.evaluate(this) as DSTExpressionOrName
        return DSTPrefixExpression(node.resolveTypeBinding(), node.operator, expression)
    }

    override fun evaluateInfixExpression(node: InfixExpression): DSTInfixExpression {

        val lhs = node.leftOperand.evaluate(this) as DSTExpressionOrName
        val rhs = node.rightOperand.evaluate(this) as DSTExpressionOrName

        if (node.hasExtendedOperands()) {
            val expressions = mutableListOf<DSTExpressionOrName>()
            expressions.add(lhs)
            expressions.add(rhs)

            for (expr in node.extendedOperands()) {
                expressions.add((expr as ASTNode).evaluate(this) as DSTExpressionOrName)
            }

            return DSTMultiInfixExpression(node.resolveTypeBinding(), node.operator, expressions)
        }

        return DSTSingleInfixExpression(node.resolveTypeBinding(), node.operator, lhs, rhs)
    }

    override fun evaluatePostfixExpression(node: PostfixExpression): DSTPostfixExpression {
        val expression = node.operand.evaluate(this) as DSTExpressionOrName
        return DSTPostfixExpression(node.resolveTypeBinding(), node.operator, expression)
    }

    //  Literals

    override fun evaluateStringLiteral(node: StringLiteral): DSTStringLiteral {
        return DSTStringLiteral(node.resolveTypeBinding(), node.escapedValue)
    }

    override fun evaluateCharacterLiteral(node: CharacterLiteral): DSTCharLiteral {
        return DSTCharLiteral(node.resolveTypeBinding(), node.escapedValue)
    }

    override fun evaluateBooleanLiteral(node: BooleanLiteral): DSTBooleanLiteral {
        return DSTBooleanLiteral(node.resolveTypeBinding(), node.booleanValue())
    }

    override fun evaluateNumberLiteral(node: NumberLiteral): DSTNumberLiteral {
        return DSTNumberLiteral(node.resolveTypeBinding(), node.token)
    }

    override fun evaluateTypeLiteral(node: TypeLiteral): DSTTypeLiteral {
        val type = node.type.evaluate(this) as DSTType
        return DSTTypeLiteral(node.resolveTypeBinding(), type)
    }

    override fun evaluateNullLiteral(node: NullLiteral): DSTNullLiteral {
        return DSTNullLiteral(node.resolveTypeBinding())
    }

    //  Comments

    override fun evaluateLineComment(node: LineComment): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateJavadoc(node: Javadoc): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateTagElement(node: TagElement): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateMethodRefParameter(node: MethodRefParameter): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateMemberRef(node: MemberRef): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateMethodRef(node: MethodRef): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateBlockComment(node: BlockComment): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateTextElement(node: TextElement): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //  Modules

    override fun evaluateOpensDirective(node: OpensDirective): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateExportsDirective(node: ExportsDirective): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateUsesDirective(node: UsesDirective): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateProvidesDirective(node: ProvidesDirective): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateRequiresDirective(node: RequiresDirective): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateModuleModifier(node: ModuleModifier): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateModuleDeclaration(node: ModuleDeclaration): DSTNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}