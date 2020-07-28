package spplagiarise.parser

import org.eclipse.jdt.core.dom.*
import spplagiarise.util.searchOverridenMethod

class BindingFinder {
    fun find(cus: List<CompilationUnit>): BindingStore {
        val visitor = BindingVisitor()
        cus.forEach { it.accept(visitor) }

        val iterator = visitor.bindingStore.bindings.iterator()
        while (iterator.hasNext()) {
            val (_, value) = iterator.next()

            if (value is IMethodBinding) {
                val overriddenMethod = value.searchOverridenMethod()
                if (overriddenMethod != null) {
                    if (!visitor.bindingStore.contains(overriddenMethod.declaringClass.key)) {
                        iterator.remove()
                    }
                }
            }
        }

        return visitor.bindingStore
    }
}

class BindingVisitor : ASTVisitor() {
    val bindingStore = BindingStore()

    override fun visit(node: PackageDeclaration): Boolean {
        val binding = node.resolveBinding()
        bindingStore[binding.key] = binding
        return super.visit(node)
    }

    override fun visit(node: TypeDeclaration): Boolean {
        val binding = node.resolveBinding()
        bindingStore[binding.key] = binding
        return super.visit(node)
    }

    override fun visit(node: EnumDeclaration): Boolean {
        val binding = node.resolveBinding()
        bindingStore[binding.key] = binding
        return super.visit(node)
    }

    override fun visit(node: EnumConstantDeclaration): Boolean {
        val binding = node.resolveVariable()
        bindingStore[binding.key] = binding
        return super.visit(node)
    }

    override fun visit(node: MethodDeclaration): Boolean {
        if (node.name.identifier == "main")
            return super.visit(node)

        val binding = node.resolveBinding()
        bindingStore[binding.key] = binding
        return super.visit(node)
    }

//    override fun visit(node: FieldDeclaration): Boolean {
//        node.fragments().forEach {
//            it as VariableDeclaration
//
//            val binding = it.resolveBinding()
//            bindingStore[binding.key] = binding
//        }
//
//        return super.visit(node)
//    }

    override fun visit(node: SingleVariableDeclaration): Boolean {
        val binding = node.resolveBinding()
        bindingStore[binding.key] = binding

        return super.visit(node)
    }

    override fun visit(node: VariableDeclarationFragment): Boolean {
        try {
            val binding = node.resolveBinding()
            bindingStore[binding.key] = binding

            return super.visit(node)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun visit(node: TypeParameter): Boolean {
        val binding = node.resolveBinding()
        bindingStore[binding.key] = binding
        return super.visit(node)
    }
}

class BindingStore {
    val bindings = mutableMapOf<String, IBinding>()

    operator fun set(key: String, value: IBinding) {
        bindings[key] = value
    }

    operator fun get(key: String): IBinding {
        return bindings[key]!!
    }

    fun remove(key: String) {
        bindings.remove(key)
    }

    fun contains(key: String): Boolean {
        return bindings[key] != null
    }
}

