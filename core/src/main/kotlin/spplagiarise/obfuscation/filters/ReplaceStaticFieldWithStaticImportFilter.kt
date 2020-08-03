package spplagiarise.obfuscation.filters

import org.eclipse.jdt.core.dom.IVariableBinding
import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.dst.visitor.DSTNodeVisitor
import spplagiarise.dst.visitor.walk
import spplagiarise.naming.DeferredNameContext
import spplagiarise.naming.DeferredNameMappingContext
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import java.lang.reflect.Modifier
import javax.inject.Inject

class ReplaceStaticFieldWithStaticImportFilter: DSTObfuscatorFilter {

    @Inject
    private lateinit var nameContext: DeferredNameContext

    @Inject
    private lateinit var mappingContext: DeferredNameMappingContext

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    private lateinit var compilationUnit: DSTCompilationUnit

    override fun visitDSTCompilationUnit(node: DSTCompilationUnit) {
        this.compilationUnit = node
    }

    override fun visitDSTFieldAccessExpression(node: DSTFieldAccessExpression) {
        val binding = node.name.binding

        if (binding is IVariableBinding) {
            if (binding.declaringClass == null) return
            if (binding.declaringClass.`package` == null || binding.declaringClass.`package`.name == "") return

            if (node.scope != null && Modifier.isStatic(binding.modifiers)) {
                if (validateUseOfName(binding, node) && canImportStaticField(binding)) {
                    if (!config.extreme && !random.randomBoolean()) return

                    if (needsToInsertImportForMethod(binding)) {
                        val import = DSTStaticFieldImportDeclaration (
                                makeNameForMethod(binding),
                                binding
                        )

                        compilationUnit.imports.add(import)
                        analyticContext.makeModification(this::class)
                    }

                    node.scope = null
                    analyticContext.makeModification(this::class)
                }
            }
        }
    }

    override fun visitDSTQualifiedName(node: DSTQualifiedName) {
        val binding = node.name.binding
        if (binding is IVariableBinding) {
            if (binding.declaringClass == null) return
            if (binding.declaringClass.`package` == null || binding.declaringClass.`package`.name == "") return

            if (Modifier.isStatic(binding.modifiers)) {
                if (validateUseOfName(binding, node) && canImportStaticField(binding)) {
                    if (!config.extreme && !random.randomBoolean()) return

                    if (needsToInsertImportForMethod(binding)) {
                        val import = DSTStaticFieldImportDeclaration (
                                makeNameForMethod(binding),
                                binding
                        )

                        compilationUnit.imports.add(import)
                        analyticContext.makeModification(this::class)
                    }

                    node.parent!!.replace(node, node.name)
                    analyticContext.makeModification(this::class)
                }
            }
        }
    }

    private fun canImportStaticField(binding: IVariableBinding): Boolean {
        return binding.declaringClass.`package` != null && binding.declaringClass.`package`.name != "" && !nameContext.containsBinding(binding)&& !binding.isEnumConstant
    }

    private fun validateUseOfName(binding: IVariableBinding, node: DSTNode): Boolean {
        val varName = if (nameContext.containsBinding(binding)) nameContext.resolveVariableNameForBinding(binding)
        else DSTKnownSimpleName(binding.name, binding)

        val nameComponent = if (varName is DSTKnownSimpleName) varName.name
        else mappingContext.getMappedName((varName as DSTDeferredSimpleName).id, false)

        val mem = object : Any() {
            var foundIdentifier: Boolean = false
        }

        val cu = compilationUnit
        cu.walk(object : DSTNodeVisitor {
            override fun visitDSTParameter(node: DSTParameter) {
                val name = node.name
                if (name is DSTDeferredSimpleName) {
                    val nameStr = mappingContext.getMappedName(name.id, false)
                    if (nameStr == nameComponent)
                        mem.foundIdentifier = true

                } else if (name is DSTKnownSimpleName) {
                    if (name.name == nameComponent)
                        mem.foundIdentifier = true
                }
            }

            override fun visitDSTLocalVariableDeclaration(node: DSTLocalVariableDeclaration) {
                val name = node.name
                if (name is DSTDeferredSimpleName) {
                    val name = mappingContext.getMappedName(name.id, false)
                    if (name == nameComponent)
                        mem.foundIdentifier = true
                }
            }

            override fun visitDSTSingleVariableDeclaration(node: DSTSingleVariableDeclaration) {
                val name = mappingContext.getMappedName(node.name.id, false)
                if (name == nameComponent)
                    mem.foundIdentifier = true
            }

            override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
                val name = mappingContext.getMappedName(node.name.id, true)
                if (name == nameComponent)
                    mem.foundIdentifier = true
            }

            override fun visitDSTFieldDeclaration(node: DSTFieldDeclaration) {
                val name = node.name
                if (name is DSTDeferredSimpleName) {
                    val name = mappingContext.getMappedName(name.id, false)
                    if (name == nameComponent)
                        mem.foundIdentifier = true
                }
            }

            override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
                if (node.name is DSTDeferredSimpleName) {
                    val name = mappingContext.getMappedName((node.name as DSTDeferredSimpleName).id, false)
                    if (name == nameComponent)
                        mem.foundIdentifier = true
                } else {
                    val name = (node.name as DSTKnownSimpleName).name
                    if (name == nameComponent)
                        mem.foundIdentifier = true
                }
            }
        })

        return !mem.foundIdentifier
    }

    private fun needsToInsertImportForMethod(binding: IVariableBinding): Boolean {
        for (import in compilationUnit.imports) {
            if (import is DSTStaticFieldImportDeclaration) {
                if (import.binding == binding)
                    return false
            }

            if (import is DSTStaticAsterixImportDeclaration) {
                val bindingClass = binding.declaringClass.erasure
                if (import.binding == bindingClass)
                    return false
            }
        }

        return true
    }

    private fun makeNameForMethod(binding: IVariableBinding): DSTName {
        val typePart = if (nameContext.containsBinding(binding.declaringClass)) nameContext.resolveTypeNameForBinding(binding.declaringClass)
        else DSTKnownSimpleName(binding.declaringClass.erasure.qualifiedName, binding)

        val varPart = if (nameContext.containsBinding(binding)) nameContext.resolveVariableNameForBinding(binding)
        else DSTKnownSimpleName(binding.name, binding)

        return DSTQualifiedName(typePart, varPart, binding)
    }
}