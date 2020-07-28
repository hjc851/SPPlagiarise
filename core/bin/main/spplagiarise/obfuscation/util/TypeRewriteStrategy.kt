package spplagiarise.obfuscation.util

import org.eclipse.jdt.core.dom.ITypeBinding
import spplagiarise.dst.*
import spplagiarise.dst.visitor.DSTNameEvaluator
import spplagiarise.dst.visitor.DSTTypeEvaluator
import spplagiarise.dst.visitor.evaluate
import spplagiarise.naming.DeferredNameContext


interface TypeRewriteStrategy : DSTTypeEvaluator<DSTType, DSTCompilationUnit>, DSTNameEvaluator<DSTName, DSTCompilationUnit>

class QualifyTypeRewriteStrategy(val nameContext: DeferredNameContext) : TypeRewriteStrategy {

    //  DSTTypeEvaluator

    override fun evaluateDSTSimpleType(node: DSTSimpleType, context: DSTCompilationUnit): DSTType {
        if (node.binding.isTypeVariable)
            return node

        val name = node.name.evaluate(this, context)
        return DSTSimpleType(name, node.binding).apply { this.parent = node.parent }
    }

    override fun evaluateDSTArrayType(node: DSTArrayType, context: DSTCompilationUnit): DSTType {
        val baseType = node.typeName.evaluate(this, context)
        return DSTArrayType(baseType, node.dimensions, node.binding).apply { this.parent = node.parent }
    }

    override fun evaluateDSTParameterisedType(node: DSTParameterisedType, context: DSTCompilationUnit): DSTType {
        val baseType = node.baseType.evaluate(this, context)

        return DSTParameterisedType(baseType, node.typeParameters, node.binding).apply { this.parent = node.parent }
    }

    override fun evaluateDSTQualifiedType(node: DSTQualifiedType, context: DSTCompilationUnit): DSTType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateDSTNameQualifiedType(node: DSTNameQualifiedType, context: DSTCompilationUnit): DSTType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateDSTWildcardType(node: DSTWildcardType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTUnionType(node: DSTUnionType, context: DSTCompilationUnit): DSTType {
        return DSTUnionType(
                node.types.map { it.evaluate(this, context) },
                node.binding
        ).apply { this.parent = node.parent }
    }

    override fun evaluateDSTIntersectionType(node: DSTIntersectionType, context: DSTCompilationUnit): DSTType {
        return DSTIntersectionType(
                node.types.map { it.evaluate(this, context) },
                node.binding
        ).apply { this.parent = node.parent }
    }

    override fun evaluateDSTTypeParameter(node: DSTTypeParameter, context: DSTCompilationUnit): DSTType {
        if (node.bounds.isNotEmpty()) {
            return DSTTypeParameter(
                    node.name,
                    node.bounds.map { it.evaluate(this, context) },
                    node.binding
            ).apply { this.parent = node.parent }
        }

        return node
    }

    //  DSTNameEvaluator

    override fun evaluateDSTKnownSimpleName(node: DSTKnownSimpleName, context: DSTCompilationUnit): DSTName {
        val typeBinding = node.binding as ITypeBinding

        if (typeBinding.declaringClass != null)
            return node

        if (typeBinding.isPrimitive)
            return node

        if (typeBinding.`package`.name == "")
            return node

        val qualifiedName = typeBinding.erasure.qualifiedName
        return DSTKnownSimpleName(qualifiedName, node.binding).apply { this.parent = node.parent }
    }

    override fun evaluateDSTDeferredSimpleName(node: DSTDeferredSimpleName, context: DSTCompilationUnit): DSTName {
        val typeBinding = node.binding as ITypeBinding
        val packageBinding = typeBinding.`package`

        if (packageBinding.name == "")
            return node

        if (typeBinding.declaringClass != null)
            return node

//        val declaringClassComponents = mutableListOf<DSTDeferredSimpleName>()
//        var declaringTypeBinding = typeBinding.declaringClass
//        while (declaringTypeBinding != null) {
//            declaringClassComponents.add(mappingContext.resolveTypeNameForBinding(declaringTypeBinding))
//            declaringTypeBinding = declaringTypeBinding.declaringClass
//        }
//
        val typeName = nameContext.resolveTypeNameForBinding(typeBinding)
        val packageName = nameContext.resolvePackageNameForBinding(packageBinding)

        val allNameComponents = mutableListOf<DSTDeferredSimpleName>()
        allNameComponents.add(packageName)
//        allNameComponents.addAll(declaringClassComponents)
        allNameComponents.add(typeName)

        val name = allNameComponents.drop(1).fold(allNameComponents.first() as DSTName) { prev, x ->
            DSTQualifiedName(prev, x, x.binding)
        }.apply { this.parent = node.parent }

        return name
    }

    override fun evaluateDSTQualifiedName(node: DSTQualifiedName, context: DSTCompilationUnit): DSTName {
        val typeBinding = node.binding as ITypeBinding

        TODO()
    }
}

class SimplifyTypeRewriteStrategy(val nameContext: DeferredNameContext) : TypeRewriteStrategy {
    override fun evaluateDSTSimpleType(node: DSTSimpleType, context: DSTCompilationUnit): DSTType {
        return DSTSimpleType(node.name.evaluate(this, context), node.binding)
    }

    override fun evaluateDSTArrayType(node: DSTArrayType, context: DSTCompilationUnit): DSTType {
        return DSTArrayType(
                node.typeName.evaluate(this, context),
                node.dimensions,
                node.binding
        ).apply { this.parent = node.parent }
    }

    override fun evaluateDSTParameterisedType(node: DSTParameterisedType, context: DSTCompilationUnit): DSTType {
        return DSTParameterisedType(
                node.baseType.evaluate(this, context),
                node.typeParameters,
                node.binding
        ).apply { this.parent = node.parent }
    }

    override fun evaluateDSTQualifiedType(node: DSTQualifiedType, context: DSTCompilationUnit): DSTType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateDSTNameQualifiedType(node: DSTNameQualifiedType, context: DSTCompilationUnit): DSTType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateDSTWildcardType(node: DSTWildcardType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTUnionType(node: DSTUnionType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTIntersectionType(node: DSTIntersectionType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTTypeParameter(node: DSTTypeParameter, context: DSTCompilationUnit): DSTType {
        return node
    }

    //  DSTNameEvaluator

    override fun evaluateDSTKnownSimpleName(node: DSTKnownSimpleName, context: DSTCompilationUnit): DSTName {
        return node
    }

    override fun evaluateDSTDeferredSimpleName(node: DSTDeferredSimpleName, context: DSTCompilationUnit): DSTName {
        return node
    }

    override fun evaluateDSTQualifiedName(node: DSTQualifiedName, context: DSTCompilationUnit): DSTName {
        val typeBinding = node.binding as ITypeBinding

        if (node.name is DSTDeferredSimpleName) {
            addDeferredImport(typeBinding, context)
        } else {
            addKnownImport(typeBinding, context)
        }

        return node.name.apply { this.parent = node.parent }
    }

    private fun addKnownImport(typeBinding: ITypeBinding, context: DSTCompilationUnit) {
        for (import in context.imports) {
            if (import.binding == typeBinding)
                return
        }

        val qualifiedName = typeBinding.erasure.qualifiedName
        context.imports.add(DSTSingleImportDeclaration(DSTKnownSimpleName(qualifiedName, typeBinding), typeBinding))
    }

    private fun addDeferredImport(typeBinding: ITypeBinding, context: DSTCompilationUnit) {
        for (import in context.imports) {
            if (import.binding == typeBinding.erasure)
                return
        }

        val packageName = nameContext.resolvePackageNameForBinding(typeBinding.`package`)
        val typeName = nameContext.resolveTypeNameForBinding(typeBinding.erasure)

        val qualifiedName = DSTQualifiedName(packageName, typeName, typeBinding)
        context.imports.add(
                DSTSingleImportDeclaration(qualifiedName, typeBinding)
        )
    }
}

class NoneTypeRewriteStrategy(val nameContext: DeferredNameContext) : TypeRewriteStrategy {
    override fun evaluateDSTSimpleType(node: DSTSimpleType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTArrayType(node: DSTArrayType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTParameterisedType(node: DSTParameterisedType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTQualifiedType(node: DSTQualifiedType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTNameQualifiedType(node: DSTNameQualifiedType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTWildcardType(node: DSTWildcardType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTUnionType(node: DSTUnionType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTIntersectionType(node: DSTIntersectionType, context: DSTCompilationUnit): DSTType {
        return node
    }

    override fun evaluateDSTTypeParameter(node: DSTTypeParameter, context: DSTCompilationUnit): DSTType {
        return node
    }

    //  DSTNameEvaluator

    override fun evaluateDSTKnownSimpleName(node: DSTKnownSimpleName, context: DSTCompilationUnit): DSTName {
        return node
    }

    override fun evaluateDSTDeferredSimpleName(node: DSTDeferredSimpleName, context: DSTCompilationUnit): DSTName {
        return node
    }

    override fun evaluateDSTQualifiedName(node: DSTQualifiedName, context: DSTCompilationUnit): DSTName {
        return node
    }
}