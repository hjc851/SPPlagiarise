package spplagiarise.ast.visitor

import org.eclipse.jdt.core.dom.*

abstract class ASTRequestor {
    abstract fun requestPackageDeclaration(node: PackageDeclaration)
    abstract fun didRequestPackageDeclaration(node: PackageDeclaration)
    abstract fun requestDimension(node: Dimension)
    abstract fun didRequestDimension(node: Dimension)
    abstract fun requestMethodRefParameter(node: MethodRefParameter)
    abstract fun didRequestMethodRefParameter(node: MethodRefParameter)
    abstract fun requestCompilationUnit(node: CompilationUnit)
    abstract fun didRequestCompilationUnit(node: CompilationUnit)
    abstract fun requestModifier(node: Modifier)
    abstract fun didRequestModifier(node: Modifier)
    abstract fun requestMemberValuePair(node: MemberValuePair)
    abstract fun didRequestMemberValuePair(node: MemberValuePair)
    abstract fun requestInitializer(node: Initializer)
    abstract fun didRequestInitializer(node: Initializer)
    abstract fun requestEnumDeclaration(node: EnumDeclaration)
    abstract fun didRequestEnumDeclaration(node: EnumDeclaration)
    abstract fun requestTypeDeclaration(node: TypeDeclaration)
    abstract fun didRequestTypeDeclaration(node: TypeDeclaration)
    abstract fun requestAnnotationTypeDeclaration(node: AnnotationTypeDeclaration)
    abstract fun didRequestAnnotationTypeDeclaration(node: AnnotationTypeDeclaration)
    abstract fun requestFieldDeclaration(node: FieldDeclaration)
    abstract fun didRequestFieldDeclaration(node: FieldDeclaration)
    abstract fun requestAnnotationTypeMemberDeclaration(node: AnnotationTypeMemberDeclaration)
    abstract fun didRequestAnnotationTypeMemberDeclaration(node: AnnotationTypeMemberDeclaration)
    abstract fun requestEnumConstantDeclaration(node: EnumConstantDeclaration)
    abstract fun didRequestEnumConstantDeclaration(node: EnumConstantDeclaration)
    abstract fun requestMethodDeclaration(node: MethodDeclaration)
    abstract fun didRequestMethodDeclaration(node: MethodDeclaration)
    abstract fun requestSingleVariableDeclaration(node: SingleVariableDeclaration)
    abstract fun didRequestSingleVariableDeclaration(node: SingleVariableDeclaration)
    abstract fun requestVariableDeclarationFragment(node: VariableDeclarationFragment)
    abstract fun didRequestVariableDeclarationFragment(node: VariableDeclarationFragment)
    abstract fun requestNameQualifiedType(node: NameQualifiedType)
    abstract fun didRequestNameQualifiedType(node: NameQualifiedType)
    abstract fun requestQualifiedType(node: QualifiedType)
    abstract fun didRequestQualifiedType(node: QualifiedType)
    abstract fun requestPrimitiveType(node: PrimitiveType)
    abstract fun didRequestPrimitiveType(node: PrimitiveType)
    abstract fun requestWildcardType(node: WildcardType)
    abstract fun didRequestWildcardType(node: WildcardType)
    abstract fun requestSimpleType(node: SimpleType)
    abstract fun didRequestSimpleType(node: SimpleType)
    abstract fun requestUnionType(node: UnionType)
    abstract fun didRequestUnionType(node: UnionType)
    abstract fun requestIntersectionType(node: IntersectionType)
    abstract fun didRequestIntersectionType(node: IntersectionType)
    abstract fun requestArrayType(node: ArrayType)
    abstract fun didRequestArrayType(node: ArrayType)
    abstract fun requestParameterizedType(node: ParameterizedType)
    abstract fun didRequestParameterizedType(node: ParameterizedType)
    abstract fun requestAnonymousClassDeclaration(node: AnonymousClassDeclaration)
    abstract fun didRequestAnonymousClassDeclaration(node: AnonymousClassDeclaration)
    abstract fun requestConstructorInvocation(node: ConstructorInvocation)
    abstract fun didRequestConstructorInvocation(node: ConstructorInvocation)
    abstract fun requestContinueStatement(node: ContinueStatement)
    abstract fun didRequestContinueStatement(node: ContinueStatement)
    abstract fun requestExpressionStatement(node: ExpressionStatement)
    abstract fun didRequestExpressionStatement(node: ExpressionStatement)
    abstract fun requestLabeledStatement(node: LabeledStatement)
    abstract fun didRequestLabeledStatement(node: LabeledStatement)
    abstract fun requestBlock(node: Block)
    abstract fun didRequestBlock(node: Block)
    abstract fun requestForStatement(node: ForStatement)
    abstract fun didRequestForStatement(node: ForStatement)
    abstract fun requestSynchronizedStatement(node: SynchronizedStatement)
    abstract fun didRequestSynchronizedStatement(node: SynchronizedStatement)
    abstract fun requestTryStatement(node: TryStatement)
    abstract fun didRequestTryStatement(node: TryStatement)
    abstract fun requestDoStatement(node: DoStatement)
    abstract fun didRequestDoStatement(node: DoStatement)
    abstract fun requestAssertStatement(node: AssertStatement)
    abstract fun didRequestAssertStatement(node: AssertStatement)
    abstract fun requestSwitchStatement(node: SwitchStatement)
    abstract fun didRequestSwitchStatement(node: SwitchStatement)
    abstract fun requestThrowStatement(node: ThrowStatement)
    abstract fun didRequestThrowStatement(node: ThrowStatement)
    abstract fun requestIfStatement(node: IfStatement)
    abstract fun didRequestIfStatement(node: IfStatement)
    abstract fun requestTypeDeclarationStatement(node: TypeDeclarationStatement)
    abstract fun didRequestTypeDeclarationStatement(node: TypeDeclarationStatement)
    abstract fun requestEnhancedForStatement(node: EnhancedForStatement)
    abstract fun didRequestEnhancedForStatement(node: EnhancedForStatement)
    abstract fun requestWhileStatement(node: WhileStatement)
    abstract fun didRequestWhileStatement(node: WhileStatement)
    abstract fun requestEmptyStatement(node: EmptyStatement)
    abstract fun didRequestEmptyStatement(node: EmptyStatement)
    abstract fun requestReturnStatement(node: ReturnStatement)
    abstract fun didRequestReturnStatement(node: ReturnStatement)
    abstract fun requestSwitchCase(node: SwitchCase)
    abstract fun didRequestSwitchCase(node: SwitchCase)
    abstract fun requestVariableDeclarationStatement(node: VariableDeclarationStatement)
    abstract fun didRequestVariableDeclarationStatement(node: VariableDeclarationStatement)
    abstract fun requestSuperConstructorInvocation(node: SuperConstructorInvocation)
    abstract fun didRequestSuperConstructorInvocation(node: SuperConstructorInvocation)
    abstract fun requestBreakStatement(node: BreakStatement)
    abstract fun didRequestBreakStatement(node: BreakStatement)
    abstract fun requestTagElement(node: TagElement)
    abstract fun didRequestTagElement(node: TagElement)
    abstract fun requestFieldAccess(node: FieldAccess)
    abstract fun didRequestFieldAccess(node: FieldAccess)
    abstract fun requestPostfixExpression(node: PostfixExpression)
    abstract fun didRequestPostfixExpression(node: PostfixExpression)
    abstract fun requestCharacterLiteral(node: CharacterLiteral)
    abstract fun didRequestCharacterLiteral(node: CharacterLiteral)
    abstract fun requestBooleanLiteral(node: BooleanLiteral)
    abstract fun didRequestBooleanLiteral(node: BooleanLiteral)
    abstract fun requestConditionalExpression(node: ConditionalExpression)
    abstract fun didRequestConditionalExpression(node: ConditionalExpression)
    abstract fun requestInstanceofExpression(node: InstanceofExpression)
    abstract fun didRequestInstanceofExpression(node: InstanceofExpression)
    abstract fun requestArrayInitializer(node: ArrayInitializer)
    abstract fun didRequestArrayInitializer(node: ArrayInitializer)
    abstract fun requestCreationReference(node: CreationReference)
    abstract fun didRequestCreationReference(node: CreationReference)
    abstract fun requestTypeMethodReference(node: TypeMethodReference)
    abstract fun didRequestTypeMethodReference(node: TypeMethodReference)
    abstract fun requestSuperMethodReference(node: SuperMethodReference)
    abstract fun didRequestSuperMethodReference(node: SuperMethodReference)
    abstract fun requestExpressionMethodReference(node: ExpressionMethodReference)
    abstract fun didRequestExpressionMethodReference(node: ExpressionMethodReference)
    abstract fun requestThisExpression(node: ThisExpression)
    abstract fun didRequestThisExpression(node: ThisExpression)
    abstract fun requestAssignment(node: Assignment)
    abstract fun didRequestAssignment(node: Assignment)
    abstract fun requestSuperFieldAccess(node: SuperFieldAccess)
    abstract fun didRequestSuperFieldAccess(node: SuperFieldAccess)
    abstract fun requestArrayCreation(node: ArrayCreation)
    abstract fun didRequestArrayCreation(node: ArrayCreation)
    abstract fun requestMarkerAnnotation(node: MarkerAnnotation)
    abstract fun didRequestMarkerAnnotation(node: MarkerAnnotation)
    abstract fun requestNormalAnnotation(node: NormalAnnotation)
    abstract fun didRequestNormalAnnotation(node: NormalAnnotation)
    abstract fun requestSingleMemberAnnotation(node: SingleMemberAnnotation)
    abstract fun didRequestSingleMemberAnnotation(node: SingleMemberAnnotation)
    abstract fun requestTypeLiteral(node: TypeLiteral)
    abstract fun didRequestTypeLiteral(node: TypeLiteral)
    abstract fun requestMethodInvocation(node: MethodInvocation)
    abstract fun didRequestMethodInvocation(node: MethodInvocation)
    abstract fun requestQualifiedName(node: QualifiedName)
    abstract fun didRequestQualifiedName(node: QualifiedName)
    abstract fun requestSimpleName(node: SimpleName)
    abstract fun didRequestSimpleName(node: SimpleName)
    abstract fun requestNumberLiteral(node: NumberLiteral)
    abstract fun didRequestNumberLiteral(node: NumberLiteral)
    abstract fun requestParenthesizedExpression(node: ParenthesizedExpression)
    abstract fun didRequestParenthesizedExpression(node: ParenthesizedExpression)
    abstract fun requestStringLiteral(node: StringLiteral)
    abstract fun didRequestStringLiteral(node: StringLiteral)
    abstract fun requestInfixExpression(node: InfixExpression)
    abstract fun didRequestInfixExpression(node: InfixExpression)
    abstract fun requestNullLiteral(node: NullLiteral)
    abstract fun didRequestNullLiteral(node: NullLiteral)
    abstract fun requestSuperMethodInvocation(node: SuperMethodInvocation)
    abstract fun didRequestSuperMethodInvocation(node: SuperMethodInvocation)
    abstract fun requestCastExpression(node: CastExpression)
    abstract fun didRequestCastExpression(node: CastExpression)
    abstract fun requestVariableDeclarationExpression(node: VariableDeclarationExpression)
    abstract fun didRequestVariableDeclarationExpression(node: VariableDeclarationExpression)
    abstract fun requestArrayAccess(node: ArrayAccess)
    abstract fun didRequestArrayAccess(node: ArrayAccess)
    abstract fun requestClassInstanceCreation(node: ClassInstanceCreation)
    abstract fun didRequestClassInstanceCreation(node: ClassInstanceCreation)
    abstract fun requestLambdaExpression(node: LambdaExpression)
    abstract fun didRequestLambdaExpression(node: LambdaExpression)
    abstract fun requestPrefixExpression(node: PrefixExpression)
    abstract fun didRequestPrefixExpression(node: PrefixExpression)
    abstract fun requestImportDeclaration(node: ImportDeclaration)
    abstract fun didRequestImportDeclaration(node: ImportDeclaration)
    abstract fun requestMemberRef(node: MemberRef)
    abstract fun didRequestMemberRef(node: MemberRef)
    abstract fun requestOpensDirective(node: OpensDirective)
    abstract fun didRequestOpensDirective(node: OpensDirective)
    abstract fun requestExportsDirective(node: ExportsDirective)
    abstract fun didRequestExportsDirective(node: ExportsDirective)
    abstract fun requestUsesDirective(node: UsesDirective)
    abstract fun didRequestUsesDirective(node: UsesDirective)
    abstract fun requestProvidesDirective(node: ProvidesDirective)
    abstract fun didRequestProvidesDirective(node: ProvidesDirective)
    abstract fun requestRequiresDirective(node: RequiresDirective)
    abstract fun didRequestRequiresDirective(node: RequiresDirective)
    abstract fun requestModuleModifier(node: ModuleModifier)
    abstract fun didRequestModuleModifier(node: ModuleModifier)
    abstract fun requestMethodRef(node: MethodRef)
    abstract fun didRequestMethodRef(node: MethodRef)
    abstract fun requestTextElement(node: TextElement)
    abstract fun didRequestTextElement(node: TextElement)
    abstract fun requestLineComment(node: LineComment)
    abstract fun didRequestLineComment(node: LineComment)
    abstract fun requestJavadoc(node: Javadoc)
    abstract fun didRequestJavadoc(node: Javadoc)
    abstract fun requestBlockComment(node: BlockComment)
    abstract fun didRequestBlockComment(node: BlockComment)
    abstract fun requestCatchClause(node: CatchClause)
    abstract fun didRequestCatchClause(node: CatchClause)
    abstract fun requestModuleDeclaration(node: ModuleDeclaration)
    abstract fun didRequestModuleDeclaration(node: ModuleDeclaration)
    abstract fun requestTypeParameter(node: TypeParameter)
    abstract fun didRequestTypeParameter(node: TypeParameter)
}

fun ASTNode.request(requestor: ASTRequestor) {
    when (this) {
        is PackageDeclaration -> {
            requestor.requestPackageDeclaration(this)
            requestor.didRequestPackageDeclaration(this)
        }
        is Dimension -> {
            requestor.requestDimension(this)
            requestor.didRequestDimension(this)
        }
        is MethodRefParameter -> {
            requestor.requestMethodRefParameter(this)
            requestor.didRequestMethodRefParameter(this)
        }
        is CompilationUnit -> {
            requestor.requestCompilationUnit(this)
            requestor.didRequestCompilationUnit(this)
        }
        is Modifier -> {
            requestor.requestModifier(this)
            requestor.didRequestModifier(this)
        }
        is MemberValuePair -> {
            requestor.requestMemberValuePair(this)
            requestor.didRequestMemberValuePair(this)
        }
        is Initializer -> {
            requestor.requestInitializer(this)
            requestor.didRequestInitializer(this)
        }
        is EnumDeclaration -> {
            requestor.requestEnumDeclaration(this)
            requestor.didRequestEnumDeclaration(this)
        }
        is TypeDeclaration -> {
            requestor.requestTypeDeclaration(this)
            requestor.didRequestTypeDeclaration(this)
        }
        is AnnotationTypeDeclaration -> {
            requestor.requestAnnotationTypeDeclaration(this)
            requestor.didRequestAnnotationTypeDeclaration(this)
        }
        is FieldDeclaration -> {
            requestor.requestFieldDeclaration(this)
            requestor.didRequestFieldDeclaration(this)
        }
        is AnnotationTypeMemberDeclaration -> {
            requestor.requestAnnotationTypeMemberDeclaration(this)
            requestor.didRequestAnnotationTypeMemberDeclaration(this)
        }
        is EnumConstantDeclaration -> {
            requestor.requestEnumConstantDeclaration(this)
            requestor.didRequestEnumConstantDeclaration(this)
        }
        is MethodDeclaration -> {
            requestor.requestMethodDeclaration(this)
            requestor.didRequestMethodDeclaration(this)
        }
        is SingleVariableDeclaration -> {
            requestor.requestSingleVariableDeclaration(this)
            requestor.didRequestSingleVariableDeclaration(this)
        }
        is VariableDeclarationFragment -> {
            requestor.requestVariableDeclarationFragment(this)
            requestor.didRequestVariableDeclarationFragment(this)
        }
        is NameQualifiedType -> {
            requestor.requestNameQualifiedType(this)
            requestor.didRequestNameQualifiedType(this)
        }
        is QualifiedType -> {
            requestor.requestQualifiedType(this)
            requestor.didRequestQualifiedType(this)
        }
        is PrimitiveType -> {
            requestor.requestPrimitiveType(this)
            requestor.didRequestPrimitiveType(this)
        }
        is WildcardType -> {
            requestor.requestWildcardType(this)
            requestor.didRequestWildcardType(this)
        }
        is SimpleType -> {
            requestor.requestSimpleType(this)
            requestor.didRequestSimpleType(this)
        }
        is UnionType -> {
            requestor.requestUnionType(this)
            requestor.didRequestUnionType(this)
        }
        is IntersectionType -> {
            requestor.requestIntersectionType(this)
            requestor.didRequestIntersectionType(this)
        }
        is ArrayType -> {
            requestor.requestArrayType(this)
            requestor.didRequestArrayType(this)
        }
        is ParameterizedType -> {
            requestor.requestParameterizedType(this)
            requestor.didRequestParameterizedType(this)
        }
        is AnonymousClassDeclaration -> {
            requestor.requestAnonymousClassDeclaration(this)
            requestor.didRequestAnonymousClassDeclaration(this)
        }
        is ConstructorInvocation -> {
            requestor.requestConstructorInvocation(this)
            requestor.didRequestConstructorInvocation(this)
        }
        is ContinueStatement -> {
            requestor.requestContinueStatement(this)
            requestor.didRequestContinueStatement(this)
        }
        is ExpressionStatement -> {
            requestor.requestExpressionStatement(this)
            requestor.didRequestExpressionStatement(this)
        }
        is LabeledStatement -> {
            requestor.requestLabeledStatement(this)
            requestor.didRequestLabeledStatement(this)
        }
        is Block -> {
            requestor.requestBlock(this)
            requestor.didRequestBlock(this)
        }
        is ForStatement -> {
            requestor.requestForStatement(this)
            requestor.didRequestForStatement(this)
        }
        is SynchronizedStatement -> {
            requestor.requestSynchronizedStatement(this)
            requestor.didRequestSynchronizedStatement(this)
        }
        is TryStatement -> {
            requestor.requestTryStatement(this)
            requestor.didRequestTryStatement(this)
        }
        is DoStatement -> {
            requestor.requestDoStatement(this)
            requestor.didRequestDoStatement(this)
        }
        is AssertStatement -> {
            requestor.requestAssertStatement(this)
            requestor.didRequestAssertStatement(this)
        }
        is SwitchStatement -> {
            requestor.requestSwitchStatement(this)
            requestor.didRequestSwitchStatement(this)
        }
        is ThrowStatement -> {
            requestor.requestThrowStatement(this)
            requestor.didRequestThrowStatement(this)
        }
        is IfStatement -> {
            requestor.requestIfStatement(this)
            requestor.didRequestIfStatement(this)
        }
        is TypeDeclarationStatement -> {
            requestor.requestTypeDeclarationStatement(this)
            requestor.didRequestTypeDeclarationStatement(this)
        }
        is EnhancedForStatement -> {
            requestor.requestEnhancedForStatement(this)
            requestor.didRequestEnhancedForStatement(this)
        }
        is WhileStatement -> {
            requestor.requestWhileStatement(this)
            requestor.didRequestWhileStatement(this)
        }
        is EmptyStatement -> {
            requestor.requestEmptyStatement(this)
            requestor.didRequestEmptyStatement(this)
        }
        is ReturnStatement -> {
            requestor.requestReturnStatement(this)
            requestor.didRequestReturnStatement(this)
        }
        is SwitchCase -> {
            requestor.requestSwitchCase(this)
            requestor.didRequestSwitchCase(this)
        }
        is VariableDeclarationStatement -> {
            requestor.requestVariableDeclarationStatement(this)
            requestor.didRequestVariableDeclarationStatement(this)
        }
        is SuperConstructorInvocation -> {
            requestor.requestSuperConstructorInvocation(this)
            requestor.didRequestSuperConstructorInvocation(this)
        }
        is BreakStatement -> {
            requestor.requestBreakStatement(this)
            requestor.didRequestBreakStatement(this)
        }
        is TagElement -> {
            requestor.requestTagElement(this)
            requestor.didRequestTagElement(this)
        }
        is FieldAccess -> {
            requestor.requestFieldAccess(this)
            requestor.didRequestFieldAccess(this)
        }
        is PostfixExpression -> {
            requestor.requestPostfixExpression(this)
            requestor.didRequestPostfixExpression(this)
        }
        is CharacterLiteral -> {
            requestor.requestCharacterLiteral(this)
            requestor.didRequestCharacterLiteral(this)
        }
        is BooleanLiteral -> {
            requestor.requestBooleanLiteral(this)
            requestor.didRequestBooleanLiteral(this)
        }
        is ConditionalExpression -> {
            requestor.requestConditionalExpression(this)
            requestor.didRequestConditionalExpression(this)
        }
        is InstanceofExpression -> {
            requestor.requestInstanceofExpression(this)
            requestor.didRequestInstanceofExpression(this)
        }
        is ArrayInitializer -> {
            requestor.requestArrayInitializer(this)
            requestor.didRequestArrayInitializer(this)
        }
        is CreationReference -> {
            requestor.requestCreationReference(this)
            requestor.didRequestCreationReference(this)
        }
        is TypeMethodReference -> {
            requestor.requestTypeMethodReference(this)
            requestor.didRequestTypeMethodReference(this)
        }
        is SuperMethodReference -> {
            requestor.requestSuperMethodReference(this)
            requestor.didRequestSuperMethodReference(this)
        }
        is ExpressionMethodReference -> {
            requestor.requestExpressionMethodReference(this)
            requestor.didRequestExpressionMethodReference(this)
        }
        is ThisExpression -> {
            requestor.requestThisExpression(this)
            requestor.didRequestThisExpression(this)
        }
        is Assignment -> {
            requestor.requestAssignment(this)
            requestor.didRequestAssignment(this)
        }
        is SuperFieldAccess -> {
            requestor.requestSuperFieldAccess(this)
            requestor.didRequestSuperFieldAccess(this)
        }
        is ArrayCreation -> {
            requestor.requestArrayCreation(this)
            requestor.didRequestArrayCreation(this)
        }
        is MarkerAnnotation -> {
            requestor.requestMarkerAnnotation(this)
            requestor.didRequestMarkerAnnotation(this)
        }
        is NormalAnnotation -> {
            requestor.requestNormalAnnotation(this)
            requestor.didRequestNormalAnnotation(this)
        }
        is SingleMemberAnnotation -> {
            requestor.requestSingleMemberAnnotation(this)
            requestor.didRequestSingleMemberAnnotation(this)
        }
        is TypeLiteral -> {
            requestor.requestTypeLiteral(this)
            requestor.didRequestTypeLiteral(this)
        }
        is MethodInvocation -> {
            requestor.requestMethodInvocation(this)
            requestor.didRequestMethodInvocation(this)
        }
        is QualifiedName -> {
            requestor.requestQualifiedName(this)
            requestor.didRequestQualifiedName(this)
        }
        is SimpleName -> {
            requestor.requestSimpleName(this)
            requestor.didRequestSimpleName(this)
        }
        is NumberLiteral -> {
            requestor.requestNumberLiteral(this)
            requestor.didRequestNumberLiteral(this)
        }
        is ParenthesizedExpression -> {
            requestor.requestParenthesizedExpression(this)
            requestor.didRequestParenthesizedExpression(this)
        }
        is StringLiteral -> {
            requestor.requestStringLiteral(this)
            requestor.didRequestStringLiteral(this)
        }
        is InfixExpression -> {
            requestor.requestInfixExpression(this)
            requestor.didRequestInfixExpression(this)
        }
        is NullLiteral -> {
            requestor.requestNullLiteral(this)
            requestor.didRequestNullLiteral(this)
        }
        is SuperMethodInvocation -> {
            requestor.requestSuperMethodInvocation(this)
            requestor.didRequestSuperMethodInvocation(this)
        }
        is CastExpression -> {
            requestor.requestCastExpression(this)
            requestor.didRequestCastExpression(this)
        }
        is VariableDeclarationExpression -> {
            requestor.requestVariableDeclarationExpression(this)
            requestor.didRequestVariableDeclarationExpression(this)
        }
        is ArrayAccess -> {
            requestor.requestArrayAccess(this)
            requestor.didRequestArrayAccess(this)
        }
        is ClassInstanceCreation -> {
            requestor.requestClassInstanceCreation(this)
            requestor.didRequestClassInstanceCreation(this)
        }
        is LambdaExpression -> {
            requestor.requestLambdaExpression(this)
            requestor.didRequestLambdaExpression(this)
        }
        is PrefixExpression -> {
            requestor.requestPrefixExpression(this)
            requestor.didRequestPrefixExpression(this)
        }
        is ImportDeclaration -> {
            requestor.requestImportDeclaration(this)
            requestor.didRequestImportDeclaration(this)
        }
        is MemberRef -> {
            requestor.requestMemberRef(this)
            requestor.didRequestMemberRef(this)
        }
        is OpensDirective -> {
            requestor.requestOpensDirective(this)
            requestor.didRequestOpensDirective(this)
        }
        is ExportsDirective -> {
            requestor.requestExportsDirective(this)
            requestor.didRequestExportsDirective(this)
        }
        is UsesDirective -> {
            requestor.requestUsesDirective(this)
            requestor.didRequestUsesDirective(this)
        }
        is ProvidesDirective -> {
            requestor.requestProvidesDirective(this)
            requestor.didRequestProvidesDirective(this)
        }
        is RequiresDirective -> {
            requestor.requestRequiresDirective(this)
            requestor.didRequestRequiresDirective(this)
        }
        is ModuleModifier -> {
            requestor.requestModuleModifier(this)
            requestor.didRequestModuleModifier(this)
        }
        is MethodRef -> {
            requestor.requestMethodRef(this)
            requestor.didRequestMethodRef(this)
        }
        is TextElement -> {
            requestor.requestTextElement(this)
            requestor.didRequestTextElement(this)
        }
        is LineComment -> {
            requestor.requestLineComment(this)
            requestor.didRequestLineComment(this)
        }
        is Javadoc -> {
            requestor.requestJavadoc(this)
            requestor.didRequestJavadoc(this)
        }
        is BlockComment -> {
            requestor.requestBlockComment(this)
            requestor.didRequestBlockComment(this)
        }
        is CatchClause -> {
            requestor.requestCatchClause(this)
            requestor.didRequestCatchClause(this)
        }
        is ModuleDeclaration -> {
            requestor.requestModuleDeclaration(this)
            requestor.didRequestModuleDeclaration(this)
        }
        is TypeParameter -> {
            requestor.requestTypeParameter(this)
            requestor.didRequestTypeParameter(this)
        }
    }
}
