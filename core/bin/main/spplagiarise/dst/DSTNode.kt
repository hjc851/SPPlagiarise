package spplagiarise.dst

import org.eclipse.jdt.core.dom.*

abstract class DSTNode : Cloneable {
    var parent: DSTNode? = null
    internal fun List<DSTNode>.assignParent() {
        this.forEach { it.parent = this@DSTNode }
    }
}

class DSTCompilationUnit(
        var packageDeclaration: DSTPackageDeclaration?,
        var imports: MutableList<DSTImportDeclaration>,
        var types: MutableList<DSTTypeDeclaration>
) : DSTNode() {
    init {
        packageDeclaration?.parent = this
        imports.assignParent()
        types.assignParent()
    }
}

class DSTPackageDeclaration(
        var name: DSTDeferredSimpleName
) : DSTNode() {
    init {
        name.parent = this
    }
}

abstract class DSTImportDeclaration : DSTNode() {
    abstract val name: DSTName
    abstract val binding: IBinding
}

class DSTSingleImportDeclaration(override var name: DSTName, override var binding: ITypeBinding) : DSTImportDeclaration() { init {
    name.parent = this
}
}

class DSTPackageImportDeclaration(override var name: DSTName, override var binding: IPackageBinding) : DSTImportDeclaration() { init {
    name.parent = this
}
}

class DSTStaticFieldImportDeclaration(override var name: DSTName, override var binding: IVariableBinding) : DSTImportDeclaration() { init {
    name.parent = this
}
}

class DSTStaticMethodImportDeclaration(override var name: DSTName, override var binding: IMethodBinding) : DSTImportDeclaration() { init {
    name.parent = this
}
}

class DSTStaticAsterixImportDeclaration(override var name: DSTName, override var binding: ITypeBinding) : DSTImportDeclaration() { init {
    name.parent = this
}
}

abstract class DSTTypeDeclaration : DSTBodyDeclaration() {
    abstract var accessModifier: AccessModifier
    abstract var name: DSTDeferredSimpleName
    abstract var bodyDeclarations: List<DSTBodyDeclaration>
    abstract var binding: ITypeBinding
}

class DSTClassOrInterfaceTypeDeclaration(
        override var accessModifier: AccessModifier,
        var isAbstract: Boolean,
        var isStatic: Boolean,
        var declarationType: TypeDeclarationType,
        override var name: DSTDeferredSimpleName,
        var genericTypes: List<DSTType>,
        var extends: DSTType?,
        var implements: List<DSTType>,
        override var bodyDeclarations: List<DSTBodyDeclaration>,
        override var binding: ITypeBinding
) : DSTTypeDeclaration() {
    init {
        name.parent = this
        genericTypes.assignParent()
        extends?.parent = this
        implements.assignParent()
        bodyDeclarations.assignParent()
    }
}

class DSTEnumTypeDeclaration(
        override var accessModifier: AccessModifier,
        override var name: DSTDeferredSimpleName,
        var implements: List<DSTType>,
        var enumConstants: List<DSTEnumConstant>,
        override var bodyDeclarations: List<DSTBodyDeclaration>,
        override var binding: ITypeBinding
) : DSTTypeDeclaration() {
    init {
        name.parent = this
        implements.assignParent()
        enumConstants.assignParent()
        bodyDeclarations.assignParent()
    }
}

class DSTEnumConstant(
        var name: DSTDeferredSimpleName,
        var arguments: List<DSTExpressionOrName>,
        var classBody: DSTAnonymousClassBody?
) : DSTBodyDeclaration() {
    init {
        name.parent = this
        arguments.assignParent()
        classBody?.parent = this
    }
}

class DSTAnnotationTypeDeclaration(
        override var accessModifier: AccessModifier,
        override var name: DSTDeferredSimpleName,
        override var bodyDeclarations: List<DSTBodyDeclaration>,
        override var binding: ITypeBinding
) : DSTTypeDeclaration()

class DSTAnnotationMember(
        var accessModifier: AccessModifier,
        var name: DSTDeferredSimpleName,
        var type: DSTType,
        var defaultValue: DSTExpressionOrName?
) : DSTBodyDeclaration()

class DSTFieldGroup(
        var accessModifier: AccessModifier,
        var modifiers: FieldModifier,
        var type: DSTType,
        var fields: List<DSTFieldDeclaration>
) : DSTBodyDeclaration() {
    init {
        type.parent = this
        fields.assignParent()
    }
}

class DSTFieldDeclaration(
        var name: DSTSimpleName,
        var extraDimensions: Int,
        var initialiser: DSTExpressionOrName?
) : DSTNode() { init {
    name.parent = this
}
}

class DSTSyntheticField (var name: DSTSyntheticName, var type: DSTType, var value: DSTLiteral): DSTBodyDeclaration() {
    init {
        name.parent = this
        type.parent = this
        value.parent = this
    }
}

abstract class DSTBodyDeclaration : DSTNode()

class DSTConstructorDeclaration(
        var accessModifier: AccessModifier,
        var modifiers: MethodModifier,
        var typeParameters: List<DSTType>,
        var name: DSTDeferredSimpleName,
        var parameters: List<DSTParameter>,
        var throws: List<DSTType>,
        var body: DSTBlockStatement?
) : DSTBodyDeclaration() {
    init {
        typeParameters.assignParent()
        name.parent = this
        parameters.assignParent()
        throws.assignParent()
        body?.parent = this
    }
}

class DSTMethodDeclaration(
        var accessModifier: AccessModifier,
        var modifiers: MethodModifier,
        var returnType: DSTType,
        var typeParameters: List<DSTType>,
        var name: DSTSimpleName,
        var parameters: List<DSTParameter>,
        var throws: List<DSTType>,
        var body: DSTBlockStatement?,
        val binding: IMethodBinding
) : DSTBodyDeclaration() {
    init {
        returnType.parent = this
        typeParameters.assignParent()
        name.parent = this
        parameters.assignParent()
        throws.assignParent()
        body?.parent = this
    }
}

class DSTParameter(
        var name: DSTSimpleName,
        var type: DSTType,
        var isVarArg: Boolean
) : DSTNode() {
    init {
        name.parent = this
        type.parent = this
    }
}

class DSTInitialiser(
        var isStatic: Boolean,
        var body: DSTBlockStatement
) : DSTBodyDeclaration() {
    init {
        body.parent = this
    }
}

class DSTDimension : DSTNode()
class DSTModifier : DSTNode()



