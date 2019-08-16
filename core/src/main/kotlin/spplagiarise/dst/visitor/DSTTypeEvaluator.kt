package spplagiarise.dst.visitor

import spplagiarise.dst.*

interface DSTTypeEvaluator<T, U> {
    abstract fun evaluateDSTSimpleType(node: DSTSimpleType, context: U): T
    abstract fun evaluateDSTArrayType(node: DSTArrayType, context: U): T
    abstract fun evaluateDSTParameterisedType(node: DSTParameterisedType, context: U): T
    abstract fun evaluateDSTQualifiedType(node: DSTQualifiedType, context: U): T
    abstract fun evaluateDSTNameQualifiedType(node: DSTNameQualifiedType, context: U): T
    abstract fun evaluateDSTWildcardType(node: DSTWildcardType, context: U): T
    abstract fun evaluateDSTUnionType(node: DSTUnionType, context: U): T
    abstract fun evaluateDSTIntersectionType(node: DSTIntersectionType, context: U): T
    abstract fun evaluateDSTTypeParameter(node: DSTTypeParameter, context: U): T
}

fun <T, U> DSTType.evaluate(evaluator: DSTTypeEvaluator<T, U>, context: U): T {
    return when (this) {
        is DSTSimpleType -> evaluator.evaluateDSTSimpleType(this, context)
        is DSTArrayType -> evaluator.evaluateDSTArrayType(this, context)
        is DSTParameterisedType -> evaluator.evaluateDSTParameterisedType(this, context)
        is DSTQualifiedType -> evaluator.evaluateDSTQualifiedType(this, context)
        is DSTNameQualifiedType -> evaluator.evaluateDSTNameQualifiedType(this, context)
        is DSTWildcardType -> evaluator.evaluateDSTWildcardType(this, context)
        is DSTUnionType -> evaluator.evaluateDSTUnionType(this, context)
        is DSTIntersectionType -> evaluator.evaluateDSTIntersectionType(this, context)
        is DSTTypeParameter -> evaluator.evaluateDSTTypeParameter(this, context)
        else -> throw IllegalArgumentException("Unknown name ${this.javaClass}")
    }
}

fun <T> DSTType.evaluate(evaluator: DSTTypeEvaluator<T, Unit>): T {
    return when (this) {
        is DSTSimpleType -> evaluator.evaluateDSTSimpleType(this, Unit)
        is DSTArrayType -> evaluator.evaluateDSTArrayType(this, Unit)
        is DSTParameterisedType -> evaluator.evaluateDSTParameterisedType(this, Unit)
        is DSTQualifiedType -> evaluator.evaluateDSTQualifiedType(this, Unit)
        is DSTNameQualifiedType -> evaluator.evaluateDSTNameQualifiedType(this, Unit)
        is DSTWildcardType -> evaluator.evaluateDSTWildcardType(this, Unit)
        is DSTUnionType -> evaluator.evaluateDSTUnionType(this, Unit)
        is DSTIntersectionType -> evaluator.evaluateDSTIntersectionType(this, Unit)
        is DSTTypeParameter -> evaluator.evaluateDSTTypeParameter(this, Unit)
        else -> throw IllegalArgumentException("Unknown name ${this.javaClass}")
    }
}
