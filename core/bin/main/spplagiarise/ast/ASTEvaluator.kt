package spplagiarise.ast

import org.eclipse.jdt.core.dom.*

abstract class ASTEvaluator<T> {
    abstract fun evaluatePackageDeclaration(node: PackageDeclaration): T
    abstract fun evaluateDimension(node: Dimension): T
    abstract fun evaluateMethodRefParameter(node: MethodRefParameter): T
    abstract fun evaluateCompilationUnit(node: CompilationUnit): T
    abstract fun evaluateModifier(node: Modifier): T
    abstract fun evaluateMemberValuePair(node: MemberValuePair): T
    abstract fun evaluateInitializer(node: Initializer): T
    abstract fun evaluateEnumDeclaration(node: EnumDeclaration): T
    abstract fun evaluateTypeDeclaration(node: TypeDeclaration): T
    abstract fun evaluateAnnotationTypeDeclaration(node: AnnotationTypeDeclaration): T
    abstract fun evaluateFieldDeclaration(node: FieldDeclaration): T
    abstract fun evaluateAnnotationTypeMemberDeclaration(node: AnnotationTypeMemberDeclaration): T
    abstract fun evaluateEnumConstantDeclaration(node: EnumConstantDeclaration): T
    abstract fun evaluateMethodDeclaration(node: MethodDeclaration): T
    abstract fun evaluateSingleVariableDeclaration(node: SingleVariableDeclaration): T
    abstract fun evaluateVariableDeclarationFragment(node: VariableDeclarationFragment): T
    abstract fun evaluateUnionType(node: UnionType): T
    abstract fun evaluateIntersectionType(node: IntersectionType): T
    abstract fun evaluateArrayType(node: ArrayType): T
    abstract fun evaluateParameterizedType(node: ParameterizedType): T
    abstract fun evaluateNameQualifiedType(node: NameQualifiedType): T
    abstract fun evaluateQualifiedType(node: QualifiedType): T
    abstract fun evaluatePrimitiveType(node: PrimitiveType): T
    abstract fun evaluateWildcardType(node: WildcardType): T
    abstract fun evaluateSimpleType(node: SimpleType): T
    abstract fun evaluateAnonymousClassDeclaration(node: AnonymousClassDeclaration): T
    abstract fun evaluateConstructorInvocation(node: ConstructorInvocation): T
    abstract fun evaluateContinueStatement(node: ContinueStatement): T
    abstract fun evaluateExpressionStatement(node: ExpressionStatement): T
    abstract fun evaluateLabeledStatement(node: LabeledStatement): T
    abstract fun evaluateBlock(node: Block): T
    abstract fun evaluateForStatement(node: ForStatement): T
    abstract fun evaluateSynchronizedStatement(node: SynchronizedStatement): T
    abstract fun evaluateTryStatement(node: TryStatement): T
    abstract fun evaluateDoStatement(node: DoStatement): T
    abstract fun evaluateAssertStatement(node: AssertStatement): T
    abstract fun evaluateSwitchStatement(node: SwitchStatement): T
    abstract fun evaluateThrowStatement(node: ThrowStatement): T
    abstract fun evaluateIfStatement(node: IfStatement): T
    abstract fun evaluateTypeDeclarationStatement(node: TypeDeclarationStatement): T
    abstract fun evaluateEnhancedForStatement(node: EnhancedForStatement): T
    abstract fun evaluateWhileStatement(node: WhileStatement): T
    abstract fun evaluateEmptyStatement(node: EmptyStatement): T
    abstract fun evaluateReturnStatement(node: ReturnStatement): T
    abstract fun evaluateSwitchCase(node: SwitchCase): T
    abstract fun evaluateVariableDeclarationStatement(node: VariableDeclarationStatement): T
    abstract fun evaluateSuperConstructorInvocation(node: SuperConstructorInvocation): T
    abstract fun evaluateBreakStatement(node: BreakStatement): T
    abstract fun evaluateTagElement(node: TagElement): T
    abstract fun evaluateFieldAccess(node: FieldAccess): T
    abstract fun evaluatePostfixExpression(node: PostfixExpression): T
    abstract fun evaluateCharacterLiteral(node: CharacterLiteral): T
    abstract fun evaluateBooleanLiteral(node: BooleanLiteral): T
    abstract fun evaluateConditionalExpression(node: ConditionalExpression): T
    abstract fun evaluateInstanceofExpression(node: InstanceofExpression): T
    abstract fun evaluateArrayInitializer(node: ArrayInitializer): T
    abstract fun evaluateCreationReference(node: CreationReference): T
    abstract fun evaluateTypeMethodReference(node: TypeMethodReference): T
    abstract fun evaluateSuperMethodReference(node: SuperMethodReference): T
    abstract fun evaluateExpressionMethodReference(node: ExpressionMethodReference): T
    abstract fun evaluateThisExpression(node: ThisExpression): T
    abstract fun evaluateAssignment(node: Assignment): T
    abstract fun evaluateSuperFieldAccess(node: SuperFieldAccess): T
    abstract fun evaluateArrayCreation(node: ArrayCreation): T
    abstract fun evaluateMarkerAnnotation(node: MarkerAnnotation): T
    abstract fun evaluateNormalAnnotation(node: NormalAnnotation): T
    abstract fun evaluateSingleMemberAnnotation(node: SingleMemberAnnotation): T
    abstract fun evaluateTypeLiteral(node: TypeLiteral): T
    abstract fun evaluateMethodInvocation(node: MethodInvocation): T
    abstract fun evaluateQualifiedName(node: QualifiedName): T
    abstract fun evaluateSimpleName(node: SimpleName): T
    abstract fun evaluateNumberLiteral(node: NumberLiteral): T
    abstract fun evaluateParenthesizedExpression(node: ParenthesizedExpression): T
    abstract fun evaluateStringLiteral(node: StringLiteral): T
    abstract fun evaluateInfixExpression(node: InfixExpression): T
    abstract fun evaluateNullLiteral(node: NullLiteral): T
    abstract fun evaluateSuperMethodInvocation(node: SuperMethodInvocation): T
    abstract fun evaluateCastExpression(node: CastExpression): T
    abstract fun evaluateVariableDeclarationExpression(node: VariableDeclarationExpression): T
    abstract fun evaluateArrayAccess(node: ArrayAccess): T
    abstract fun evaluateClassInstanceCreation(node: ClassInstanceCreation): T
    abstract fun evaluateLambdaExpression(node: LambdaExpression): T
    abstract fun evaluatePrefixExpression(node: PrefixExpression): T
    abstract fun evaluateImportDeclaration(node: ImportDeclaration): T
    abstract fun evaluateMemberRef(node: MemberRef): T
    abstract fun evaluateOpensDirective(node: OpensDirective): T
    abstract fun evaluateExportsDirective(node: ExportsDirective): T
    abstract fun evaluateUsesDirective(node: UsesDirective): T
    abstract fun evaluateProvidesDirective(node: ProvidesDirective): T
    abstract fun evaluateRequiresDirective(node: RequiresDirective): T
    abstract fun evaluateModuleModifier(node: ModuleModifier): T
    abstract fun evaluateMethodRef(node: MethodRef): T
    abstract fun evaluateTextElement(node: TextElement): T
    abstract fun evaluateLineComment(node: LineComment): T
    abstract fun evaluateJavadoc(node: Javadoc): T
    abstract fun evaluateBlockComment(node: BlockComment): T
    abstract fun evaluateCatchClause(node: CatchClause): T
    abstract fun evaluateModuleDeclaration(node: ModuleDeclaration): T
    abstract fun evaluateTypeParameter(node: TypeParameter): T
}

fun <T> ASTNode.evaluate(evaluator: ASTEvaluator<T>): T {
    return when (this) {
        is PackageDeclaration -> evaluator.evaluatePackageDeclaration(this)
        is Dimension -> evaluator.evaluateDimension(this)
        is MethodRefParameter -> evaluator.evaluateMethodRefParameter(this)
        is CompilationUnit -> evaluator.evaluateCompilationUnit(this)
        is Modifier -> evaluator.evaluateModifier(this)
        is MemberValuePair -> evaluator.evaluateMemberValuePair(this)
        is Initializer -> evaluator.evaluateInitializer(this)
        is EnumDeclaration -> evaluator.evaluateEnumDeclaration(this)
        is TypeDeclaration -> evaluator.evaluateTypeDeclaration(this)
        is AnnotationTypeDeclaration -> evaluator.evaluateAnnotationTypeDeclaration(this)
        is FieldDeclaration -> evaluator.evaluateFieldDeclaration(this)
        is AnnotationTypeMemberDeclaration -> evaluator.evaluateAnnotationTypeMemberDeclaration(this)
        is EnumConstantDeclaration -> evaluator.evaluateEnumConstantDeclaration(this)
        is MethodDeclaration -> evaluator.evaluateMethodDeclaration(this)
        is SingleVariableDeclaration -> evaluator.evaluateSingleVariableDeclaration(this)
        is VariableDeclarationFragment -> evaluator.evaluateVariableDeclarationFragment(this)
        is UnionType -> evaluator.evaluateUnionType(this)
        is IntersectionType -> evaluator.evaluateIntersectionType(this)
        is ArrayType -> evaluator.evaluateArrayType(this)
        is ParameterizedType -> evaluator.evaluateParameterizedType(this)
        is NameQualifiedType -> evaluator.evaluateNameQualifiedType(this)
        is QualifiedType -> evaluator.evaluateQualifiedType(this)
        is PrimitiveType -> evaluator.evaluatePrimitiveType(this)
        is WildcardType -> evaluator.evaluateWildcardType(this)
        is SimpleType -> evaluator.evaluateSimpleType(this)
        is AnonymousClassDeclaration -> evaluator.evaluateAnonymousClassDeclaration(this)
        is ConstructorInvocation -> evaluator.evaluateConstructorInvocation(this)
        is ContinueStatement -> evaluator.evaluateContinueStatement(this)
        is ExpressionStatement -> evaluator.evaluateExpressionStatement(this)
        is LabeledStatement -> evaluator.evaluateLabeledStatement(this)
        is Block -> evaluator.evaluateBlock(this)
        is ForStatement -> evaluator.evaluateForStatement(this)
        is SynchronizedStatement -> evaluator.evaluateSynchronizedStatement(this)
        is TryStatement -> evaluator.evaluateTryStatement(this)
        is DoStatement -> evaluator.evaluateDoStatement(this)
        is AssertStatement -> evaluator.evaluateAssertStatement(this)
        is SwitchStatement -> evaluator.evaluateSwitchStatement(this)
        is ThrowStatement -> evaluator.evaluateThrowStatement(this)
        is IfStatement -> evaluator.evaluateIfStatement(this)
        is TypeDeclarationStatement -> evaluator.evaluateTypeDeclarationStatement(this)
        is EnhancedForStatement -> evaluator.evaluateEnhancedForStatement(this)
        is WhileStatement -> evaluator.evaluateWhileStatement(this)
        is EmptyStatement -> evaluator.evaluateEmptyStatement(this)
        is ReturnStatement -> evaluator.evaluateReturnStatement(this)
        is SwitchCase -> evaluator.evaluateSwitchCase(this)
        is VariableDeclarationStatement -> evaluator.evaluateVariableDeclarationStatement(this)
        is SuperConstructorInvocation -> evaluator.evaluateSuperConstructorInvocation(this)
        is BreakStatement -> evaluator.evaluateBreakStatement(this)
        is TagElement -> evaluator.evaluateTagElement(this)
        is FieldAccess -> evaluator.evaluateFieldAccess(this)
        is PostfixExpression -> evaluator.evaluatePostfixExpression(this)
        is CharacterLiteral -> evaluator.evaluateCharacterLiteral(this)
        is BooleanLiteral -> evaluator.evaluateBooleanLiteral(this)
        is ConditionalExpression -> evaluator.evaluateConditionalExpression(this)
        is InstanceofExpression -> evaluator.evaluateInstanceofExpression(this)
        is ArrayInitializer -> evaluator.evaluateArrayInitializer(this)
        is CreationReference -> evaluator.evaluateCreationReference(this)
        is TypeMethodReference -> evaluator.evaluateTypeMethodReference(this)
        is SuperMethodReference -> evaluator.evaluateSuperMethodReference(this)
        is ExpressionMethodReference -> evaluator.evaluateExpressionMethodReference(this)
        is ThisExpression -> evaluator.evaluateThisExpression(this)
        is Assignment -> evaluator.evaluateAssignment(this)
        is SuperFieldAccess -> evaluator.evaluateSuperFieldAccess(this)
        is ArrayCreation -> evaluator.evaluateArrayCreation(this)
        is MarkerAnnotation -> evaluator.evaluateMarkerAnnotation(this)
        is NormalAnnotation -> evaluator.evaluateNormalAnnotation(this)
        is SingleMemberAnnotation -> evaluator.evaluateSingleMemberAnnotation(this)
        is TypeLiteral -> evaluator.evaluateTypeLiteral(this)
        is MethodInvocation -> evaluator.evaluateMethodInvocation(this)
        is QualifiedName -> evaluator.evaluateQualifiedName(this)
        is SimpleName -> evaluator.evaluateSimpleName(this)
        is NumberLiteral -> evaluator.evaluateNumberLiteral(this)
        is ParenthesizedExpression -> evaluator.evaluateParenthesizedExpression(this)
        is StringLiteral -> evaluator.evaluateStringLiteral(this)
        is InfixExpression -> evaluator.evaluateInfixExpression(this)
        is NullLiteral -> evaluator.evaluateNullLiteral(this)
        is SuperMethodInvocation -> evaluator.evaluateSuperMethodInvocation(this)
        is CastExpression -> evaluator.evaluateCastExpression(this)
        is VariableDeclarationExpression -> evaluator.evaluateVariableDeclarationExpression(this)
        is ArrayAccess -> evaluator.evaluateArrayAccess(this)
        is ClassInstanceCreation -> evaluator.evaluateClassInstanceCreation(this)
        is LambdaExpression -> evaluator.evaluateLambdaExpression(this)
        is PrefixExpression -> evaluator.evaluatePrefixExpression(this)
        is ImportDeclaration -> evaluator.evaluateImportDeclaration(this)
        is MemberRef -> evaluator.evaluateMemberRef(this)
        is OpensDirective -> evaluator.evaluateOpensDirective(this)
        is ExportsDirective -> evaluator.evaluateExportsDirective(this)
        is UsesDirective -> evaluator.evaluateUsesDirective(this)
        is ProvidesDirective -> evaluator.evaluateProvidesDirective(this)
        is RequiresDirective -> evaluator.evaluateRequiresDirective(this)
        is ModuleModifier -> evaluator.evaluateModuleModifier(this)
        is MethodRef -> evaluator.evaluateMethodRef(this)
        is TextElement -> evaluator.evaluateTextElement(this)
        is LineComment -> evaluator.evaluateLineComment(this)
        is Javadoc -> evaluator.evaluateJavadoc(this)
        is BlockComment -> evaluator.evaluateBlockComment(this)
        is CatchClause -> evaluator.evaluateCatchClause(this)
        is ModuleDeclaration -> evaluator.evaluateModuleDeclaration(this)
        is TypeParameter -> evaluator.evaluateTypeParameter(this)

        else -> throw IllegalArgumentException("Unknown node name ${this.javaClass}")
    }
}