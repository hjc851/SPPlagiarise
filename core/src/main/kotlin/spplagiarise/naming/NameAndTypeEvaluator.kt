package spplagiarise.naming

import org.eclipse.jdt.core.dom.ITypeBinding
import spplagiarise.dst.*
import spplagiarise.dst.visitor.DSTNameAndTypeEvaluator
import spplagiarise.dst.visitor.evaluate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NameAndTypeEvaluator : DSTNameAndTypeEvaluator<String, Unit> {

    @Inject
    private lateinit var nameMatchContext: DeferredNameMappingContext

    override fun evaluateDSTKnownSimpleName(node: DSTKnownSimpleName, context: Unit): String {
        return node.name
    }

    override fun evaluateDSTDeferredSimpleName(node: DSTDeferredSimpleName, context: Unit): String {
        val ucFirst = if (node.binding is ITypeBinding) true else false
        val mappedName = nameMatchContext.getMappedName(node.id, ucFirst)
        return mappedName
    }

    override fun evaluateDSTQualifiedName(node: DSTQualifiedName, context: Unit): String {
        val name = "${node.qualifier.evaluate(this, Unit)}.${node.name.evaluate(this, Unit)}"
        return name
    }

    override fun evaluateDSTSimpleType(node: DSTSimpleType, context: Unit): String {
        return node.name.evaluate(this, Unit)
    }

    override fun evaluateDSTArrayType(node: DSTArrayType, context: Unit): String {
        val builder = StringBuilder()
        builder.append(node.typeName.evaluate(this, Unit))

        for (i in 0 until node.dimensions)
            builder.append("[]")

        return builder.toString()
    }

    override fun evaluateDSTParameterisedType(node: DSTParameterisedType, context: Unit): String {
        val builder = StringBuilder()
        builder.append(node.baseType.evaluate(this, context))

        builder.append('<')
        node.typeParameters.forEachIndexed { index, dstType ->
            builder.append(dstType.evaluate(this, context))

            if (index < node.typeParameters.size - 1)
                builder.append(", ")
        }
        builder.append('>')

        return builder.toString()
    }

    override fun evaluateDSTQualifiedType(node: DSTQualifiedType, context: Unit): String {
        val name = "${node.qualifier.evaluate(this, Unit)}.${node.name.evaluate(this, Unit)}"
        return name
    }

    override fun evaluateDSTNameQualifiedType(node: DSTNameQualifiedType, context: Unit): String {
        val name = "${node.qualifier.evaluate(this, Unit)}.${node.name.evaluate(this, Unit)}"
        return name
    }

    override fun evaluateDSTWildcardType(node: DSTWildcardType, context: Unit): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateDSTUnionType(node: DSTUnionType, context: Unit): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateDSTIntersectionType(node: DSTIntersectionType, context: Unit): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateDSTTypeParameter(node: DSTTypeParameter, context: Unit): String {
        val builder = StringBuilder()

        builder.append(node.binding.name)

//        builder.append(node.name.evaluate(this, Unit))

        if (node.bounds.isNotEmpty()) {
            builder.append(" extends ")

            node.bounds.forEachIndexed { index, dstType ->
                builder.append(dstType.evaluate(this, Unit))

                if (index < node.bounds.size - 1)
                    print(" & ")
            }
        }

        return builder.toString()
    }
}