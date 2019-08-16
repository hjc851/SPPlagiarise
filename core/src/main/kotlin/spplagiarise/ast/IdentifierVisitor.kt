package spplagiarise.ast

import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.QualifiedName
import org.eclipse.jdt.core.dom.SimpleName
import javax.inject.Singleton

@Singleton
class IdentifierVisitor : ASTVisitor() {
    val identifiers: MutableSet<String> = mutableSetOf()

    override fun visit(node: SimpleName): Boolean {
        identifiers.add(node.identifier)
        return false
    }

    override fun visit(node: QualifiedName): Boolean {
        identifiers.add(node.fullyQualifiedName)
        return false
    }
}