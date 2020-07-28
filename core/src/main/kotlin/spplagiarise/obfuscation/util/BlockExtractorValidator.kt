package spplagiarise.obfuscation.util

import org.eclipse.jdt.core.dom.IVariableBinding
import spplagiarise.dst.*
import spplagiarise.dst.visitor.DSTNodeVisitor
import spplagiarise.dst.visitor.evaluate
import java.lang.reflect.Modifier
import javax.inject.Inject

class BlockExtractorValidator : DSTNodeVisitor {

    @Inject
    private lateinit var typeFactory: DSTTypeFactory

    private var methodsThrowExceptions = false

    fun blockCanBeExtracted(): Boolean {
        val assignsToButDoesntDeclare = assignedLocals.map { it.binding }.toSet() - declaredNames.map { it.binding }.toSet()

        return (assignsToButDoesntDeclare.isEmpty() && !hasControlStatement && !methodsThrowExceptions)
    }

    fun refactoredParameters(): List<DSTParameter> {

        val refNames = referencedNames.groupBy { it.binding }
                .map { it.value.first() }

        val decBindings = declaredNames.map { it.binding }
                .distinct()

        val parameters = refNames.filterNot { decBindings.contains(it.binding) }
                .filterNot { Modifier.isStatic(it.binding.modifiers) }
                .filterNot { (it.binding as IVariableBinding).isField }
                .filterNot { (it.binding as IVariableBinding).isEnumConstant }
                .map {
                    val binding = it.binding as IVariableBinding
                    typeFactory.produceTypeFromBinding(binding.type) to it.clone()
                }.map { DSTParameter(it.second, it.first, false, 0) }

        return parameters
    }

    private val assignedLocals = mutableSetOf<DSTSimpleName>()
    private val referencedNames = mutableSetOf<DSTSimpleName>()
    private val declaredNames = mutableSetOf<DSTSimpleName>()
    private var hasControlStatement = false

    override fun visitDSTAssignment(node: DSTAssignment) {
        val assignee = node.assignee
        if (assignee is DSTSimpleName) {
            val binding = assignee.binding
            if (binding is IVariableBinding) {
                if (!binding.isEnumConstant && !binding.isParameter && !binding.isField) {
                    // Must be a local
                    assignedLocals.add(assignee)
                }
            }
        }
    }

    override fun visitDSTMethodCall(node: DSTMethodCall) {
        if (node.binding.exceptionTypes.isNotEmpty())
            methodsThrowExceptions = true
    }

    //  Declarations

    override fun visitDSTLocalVariableDeclaration(node: DSTLocalVariableDeclaration) {
        declaredNames.add(node.name)
    }

    override fun visitDSTSingleVariableDeclaration(node: DSTSingleVariableDeclaration) {
        declaredNames.add(node.name)
    }

    // Control Flow


    override fun visitDSTBreakStatement(node: DSTBreakStatement) {
        hasControlStatement = true
    }

    override fun visitDSTContinueStatement(node: DSTContinueStatement) {
        hasControlStatement = true
    }

    override fun visitDSTReturnStatement(node: DSTReturnStatement) {
        hasControlStatement = true
    }

    override fun visitDSTThrowStatement(node: DSTThrowStatement) {
        hasControlStatement = true
    }

    // Root block

    override fun visitDSTBlockStatement(node: DSTBlockStatement) {
        val referencedNames = node.evaluate(ExpressionBindingResolver)
        this.referencedNames.addAll(referencedNames)
    }
}