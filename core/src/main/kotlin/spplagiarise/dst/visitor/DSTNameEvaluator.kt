package spplagiarise.dst.visitor

import spplagiarise.dst.DSTDeferredSimpleName
import spplagiarise.dst.DSTKnownSimpleName
import spplagiarise.dst.DSTName
import spplagiarise.dst.DSTQualifiedName

interface DSTNameEvaluator<T, U> {
    abstract fun evaluateDSTKnownSimpleName(node: DSTKnownSimpleName, context: U): T
    abstract fun evaluateDSTDeferredSimpleName(node: DSTDeferredSimpleName, context: U): T
    abstract fun evaluateDSTQualifiedName(node: DSTQualifiedName, context: U): T
}

fun <T, U> DSTName.evaluate(evaluator: DSTNameEvaluator<T, U>, context: U): T {
    return when (this) {
        is DSTKnownSimpleName -> evaluator.evaluateDSTKnownSimpleName(this, context)
        is DSTDeferredSimpleName -> evaluator.evaluateDSTDeferredSimpleName(this, context)
        is DSTQualifiedName -> evaluator.evaluateDSTQualifiedName(this, context)
        else -> throw IllegalArgumentException("Unknown name ${this.javaClass}")
    }
}

fun <T> DSTName.evaluate(evaluator: DSTNameEvaluator<T, Unit>): T {
    return when (this) {
        is DSTKnownSimpleName -> evaluator.evaluateDSTKnownSimpleName(this, Unit)
        is DSTDeferredSimpleName -> evaluator.evaluateDSTDeferredSimpleName(this, Unit)
        is DSTQualifiedName -> evaluator.evaluateDSTQualifiedName(this, Unit)
        else -> throw IllegalArgumentException("Unknown name ${this.javaClass}")
    }
}