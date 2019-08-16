package spplagiarise.util

import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import java.lang.Exception
import java.util.*

fun IMethodBinding.searchOverridenMethod(): IMethodBinding? {
    val toCheck = Stack<ITypeBinding>()


    if (this.declaringClass.superclass != null)
        toCheck.push(this.declaringClass.superclass)

    toCheck.addAll(this.declaringClass.interfaces)


    try {
        while (toCheck.isNotEmpty()) {
            val nextType = toCheck.pop()

            val methods = nextType.declaredMethods
                    .filter { it.name == this.name && it.returnType == this.returnType }

            for (method in methods) {
                if (this.overrides(method))
                    return method
            }

            if (nextType.superclass != null)
                toCheck.push(nextType.superclass)

            toCheck.addAll(nextType.interfaces)
        }
    } catch (e: Exception) {
        throw e
    }

    return null
}