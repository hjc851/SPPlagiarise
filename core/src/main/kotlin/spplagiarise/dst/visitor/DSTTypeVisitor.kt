package spplagiarise.dst.visitor

import spplagiarise.dst.*

abstract class DSTTypeVisitor {
    abstract fun visitDSTSimpleType(node: DSTSimpleType)
    abstract fun visitDSTArrayType(node: DSTArrayType)
    abstract fun visitDSTParameterisedType(node: DSTParameterisedType)
    abstract fun visitDSTQualifiedType(node: DSTQualifiedType)
    abstract fun visitDSTNameQualifiedType(node: DSTNameQualifiedType)
    abstract fun visitDSTWildcardType(node: DSTWildcardType)
    abstract fun visitDSTUnionType(node: DSTUnionType)
    abstract fun visitDSTIntersectionType(node: DSTIntersectionType)
    abstract fun visitDSTTypeParameter(node: DSTTypeParameter)
}

fun DSTType.accept(visitor: DSTTypeVisitor) {
    when (this) {
        is DSTSimpleType -> visitor.visitDSTSimpleType(this)
        is DSTArrayType -> visitor.visitDSTArrayType(this)
        is DSTParameterisedType -> visitor.visitDSTParameterisedType(this)
        is DSTQualifiedType -> visitor.visitDSTQualifiedType(this)
        is DSTNameQualifiedType -> visitor.visitDSTNameQualifiedType(this)
        is DSTWildcardType -> visitor.visitDSTWildcardType(this)
        is DSTUnionType -> visitor.visitDSTUnionType(this)
        is DSTIntersectionType -> visitor.visitDSTIntersectionType(this)
        is DSTTypeParameter -> visitor.visitDSTTypeParameter(this)
        else -> throw IllegalArgumentException("Unknown name ${this.javaClass}")
    }
}