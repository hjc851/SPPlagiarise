package spplagiarise.dst.visitor

import spplagiarise.dst.DSTDeferredSimpleName
import spplagiarise.dst.DSTKnownSimpleName
import spplagiarise.dst.DSTName
import spplagiarise.dst.DSTQualifiedName

abstract class DSTNameVisitor {
    abstract fun visitDSTKnownSimpleName(node: DSTKnownSimpleName)
    abstract fun visitDSTDeferredSimpleName(node: DSTDeferredSimpleName)
    abstract fun visitDSTQualifiedName(node: DSTQualifiedName)
}

fun DSTName.accept(visitor: DSTNameVisitor) {
    when (this) {
        is DSTKnownSimpleName -> visitor.visitDSTKnownSimpleName(this)
        is DSTDeferredSimpleName -> visitor.visitDSTDeferredSimpleName(this)
        is DSTQualifiedName -> visitor.visitDSTQualifiedName(this)
        else -> throw IllegalArgumentException("Unknown name ${this.javaClass}")
    }
}
