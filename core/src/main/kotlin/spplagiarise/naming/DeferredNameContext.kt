package spplagiarise.naming

import org.eclipse.jdt.core.dom.*
import spplagiarise.cdi.ObfuscatorScoped
import spplagiarise.dst.DSTDeferredSimpleName
import spplagiarise.util.searchOverridenMethod
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Singleton

@ObfuscatorScoped
open class DeferredNameContext {
    private val nameCounter = AtomicInteger(0)

    private val keyMappings = ConcurrentHashMap<String, Int>()
    private val bindingMappings = ConcurrentHashMap<Int, IBinding>()

    open fun bindingForId(id: Int): IBinding {
        return bindingMappings[id]!!
    }

    open fun containsBinding(binding: IBinding): Boolean {
        return keyMappings.containsKey(binding.key)
    }

    open fun resolveNameForBinding(binding: IBinding): DSTDeferredSimpleName {
        if (binding is IMethodBinding)
            return resolveMethodNameForBinding(binding)

        val id = keyMappings.computeIfAbsent(binding.key) {
            val theId = nameCounter.getAndIncrement()
            bindingMappings[theId] = binding
            return@computeIfAbsent theId
        }

        return DSTDeferredSimpleName(id, binding)
    }

    open fun resolvePackageNameForBinding(binding: IPackageBinding): DSTDeferredSimpleName = resolveNameForBinding(binding)
    open fun resolveTypeNameForBinding(binding: ITypeBinding): DSTDeferredSimpleName = resolveNameForBinding(binding)
    open fun resolveVariableNameForBinding(binding: IVariableBinding): DSTDeferredSimpleName = resolveNameForBinding(binding)

    open fun resolveMethodNameForBinding(binding: IMethodBinding): DSTDeferredSimpleName {
        var overriddenMethod = binding.searchOverridenMethod()
        if (overriddenMethod != null) {
            return resolveMethodNameForBinding(overriddenMethod)
        }

        val id = keyMappings.computeIfAbsent(binding.key) {
            val theId = nameCounter.getAndIncrement()
            bindingMappings[theId] = binding
            return@computeIfAbsent theId
        }

        return DSTDeferredSimpleName(id, binding)
    }
}

