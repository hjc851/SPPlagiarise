package spplagiarise.dst

import org.eclipse.jdt.core.dom.IBinding
import org.eclipse.jdt.core.dom.ITypeBinding

//  Names

abstract class DSTName : DSTExpressionOrName() {
    abstract val binding: IBinding
}

abstract class DSTSimpleName : DSTName()
class DSTKnownSimpleName(var name: String, override var binding: IBinding) : DSTSimpleName()
class DSTDeferredSimpleName(var id: Int, override var binding: IBinding) : DSTSimpleName()

class DSTSyntheticName(val name: String, val type: ITypeBinding): DSTExpressionOrName()

class DSTQualifiedName(
        var qualifier: DSTName,
        var name: DSTSimpleName,
        override var binding: IBinding
) : DSTName() {
    init {
        qualifier.parent = this
        name.parent = this
    }
}

//  Types

abstract class DSTType : DSTNode() {
    abstract var binding: ITypeBinding
}

class DSTSimpleType(var name: DSTName, override var binding: ITypeBinding) : DSTType() { init {
    name.parent = this
}
}

class DSTArrayType(var typeName: DSTType, var dimensions: Int, override var binding: ITypeBinding) : DSTType() { init {
    typeName.parent = this
}
}

class DSTParameterisedType(
        var baseType: DSTType,
        var typeParameters: List<DSTType>,
        override var binding: ITypeBinding
) : DSTType() {
    init {
        baseType.parent = this
        typeParameters.assignParent()
    }
}

class DSTQualifiedType(
        var qualifier: DSTType,
        var name: DSTSimpleName,
        override var binding: ITypeBinding
) : DSTType() {
    init {
        qualifier.parent = this
        name.parent = this
    }
}

class DSTNameQualifiedType(
        var qualifier: DSTName,
        var name: DSTSimpleName,
        override var binding: ITypeBinding
) : DSTType() {
    init {
        qualifier.parent = this
        name.parent = this
    }
}

class DSTWildcardType(
        bound: DSTType?,
        isUpperBound: Boolean,
        override var binding: ITypeBinding
) : DSTType() {
    init {
        bound?.parent = this
    }
}

class DSTUnionType(
        var types: List<DSTType>,
        override var binding: ITypeBinding
) : DSTType() {
    init {
        types.assignParent()
    }
}

class DSTIntersectionType(
        var types: List<DSTType>,
        override var binding: ITypeBinding
) : DSTType() {
    init {
        types.assignParent()
    }
}

class DSTTypeParameter(
        var name: DSTName,
        var bounds: List<DSTType>,
        override var binding: ITypeBinding
) : DSTType() {
    init {
        name.parent = this
        bounds.assignParent()
    }
}