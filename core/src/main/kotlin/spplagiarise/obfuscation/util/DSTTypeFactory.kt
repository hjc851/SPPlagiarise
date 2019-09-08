package spplagiarise.obfuscation.util

import org.eclipse.jdt.core.dom.IPackageBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import spplagiarise.dst.*
import spplagiarise.naming.DeferredNameContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DSTTypeFactory {
    @Inject
    private lateinit var nameContext: DeferredNameContext

    fun produceTypeFromBinding(binding: ITypeBinding): DSTType {

        if (binding.isParameterizedType)
            return produceParameterisedType(binding)

        if (binding.isTypeVariable)
            return produceTypeVariable(binding)

        if (binding.isRawType)
            return DSTSimpleType(DSTKnownSimpleName(binding.qualifiedName, binding), binding)

        if (binding.isCapture)
            TODO()

        if (binding.isGenericType)
            TODO()

        if (binding.isArray)
            return produceArrayType(binding)

        if (binding.isAnonymous)
            TODO()

        if (binding.isIntersectionType)
            TODO()

        if (binding.isWildcardType)
            TODO()

        val name = produceName(binding)
        return DSTSimpleType(name, binding)
    }

    private fun produceArrayType(binding: ITypeBinding): DSTType {
        val baseType = produceTypeFromBinding(binding.elementType)
        val size = binding.dimensions

        return DSTArrayType(baseType, size, binding)
    }

    private fun produceParameterisedType(binding: ITypeBinding): DSTType {
        val element = binding.erasure

        val parameters = binding.typeArguments.map { produceTypeFromBinding(it) }
        val elementName = DSTKnownSimpleName(element.qualifiedName, binding.erasure)

        return DSTParameterisedType(DSTSimpleType(elementName, element), parameters, binding)
    }

    fun produceBoundTypeVariable(binding: ITypeBinding): DSTType {
//        val name = if (nameContext.containsBinding(binding))
//            nameContext.resolveNameForBinding(binding)
//        else
//            DSTKnownSimpleName(binding.name, binding)
//
//        val bounds = binding.typeBounds.map { this.produceTypeFromBinding(it) }
//
//        return DSTTypeParameter(name, bounds, binding)

        TODO()
    }

    private fun produceTypeVariable(binding: ITypeBinding): DSTType {
        val name = if (nameContext.containsBinding(binding))
            nameContext.resolveNameForBinding(binding)
        else
            DSTKnownSimpleName(binding.name, binding)

        return DSTSimpleType(name, binding)
    }

    private fun produceName(binding: ITypeBinding): DSTName {
        val binding = binding.erasure
        val pkg = binding.`package`

        return getTypeNameMapping(binding)

        if (binding.declaringClass != null)
            TODO()

        if (pkg == null || pkg.name == "")
            return getTypeNameMapping(binding)

        val namePart = getTypeNameMapping(binding)
        val pkgPart = getPackageNameMapping(pkg)

        return DSTQualifiedName(pkgPart, namePart, binding)
    }

    fun getTypeNameMapping(binding: ITypeBinding): DSTSimpleName {
        if (nameContext.containsBinding(binding))
            return nameContext.resolveNameForBinding(binding)

        return DSTKnownSimpleName(binding.name, binding)
    }

    private fun getPackageNameMapping(binding: IPackageBinding): DSTSimpleName {
        if (nameContext.containsBinding(binding))
            return nameContext.resolveNameForBinding(binding)

        return DSTKnownSimpleName(binding.name, binding)
    }
}
