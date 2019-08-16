package spplagiarise.obfuscation.l5

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.obfuscation.util.DSTTypeFactory
import spplagiarise.util.IRandomGenerator
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

private val syntheticVariableCounter = AtomicInteger(0)

class ReplaceConstantWithVariableFilter: DSTObfuscatorFilter {
    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var typeFactory: DSTTypeFactory

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    private lateinit var currentType: DSTTypeDeclaration
    private var isMethod: Boolean = false
    private var isInnerClass: Boolean = false

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        currentType = node
        isInnerClass = node.binding.declaringClass != null
    }

    override fun visitDSTEnumTypeDeclaration(node: DSTEnumTypeDeclaration) {
        currentType = node
        isInnerClass = node.binding.declaringClass != null
    }

    override fun visitDSTFieldGroup(node: DSTFieldGroup) {
        isMethod = false
    }

    override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
        isMethod = true
    }

    //  Literals

    override fun visitDSTBooleanLiteral(node: DSTBooleanLiteral) {
        if (!isMethod) return
        if (isInnerClass) return
        if (node.parent == null) return
        if (!config.extreme && !random.randomBoolean()) return
        val name = DSTSyntheticName("syn" + "X" + syntheticVariableCounter.getAndIncrement() + node.typeBinding.name, node.typeBinding)
        val field = DSTSyntheticField(name.clone(), typeFactory.produceTypeFromBinding(node.typeBinding), node.clone())

        node.parent!!.replace(node, name)
        val body = currentType.bodyDeclarations.toMutableList()
        body.add(0, field)
        currentType.bodyDeclarations = body
        analyticContext.makeModification(this::class)
    }

    override fun visitDSTCharLiteral(node: DSTCharLiteral) {
        if (!isMethod) return
        if (isInnerClass) return
        if (node.parent == null) return
        if (!config.extreme && !random.randomBoolean()) return
        val name = DSTSyntheticName("syn" + "X" + syntheticVariableCounter.getAndIncrement() + node.typeBinding.name, node.typeBinding)
        val field = DSTSyntheticField(name.clone(), typeFactory.produceTypeFromBinding(node.typeBinding), node.clone())

        node.parent!!.replace(node, name)
        val body = currentType.bodyDeclarations.toMutableList()
        body.add(0, field)
        currentType.bodyDeclarations = body
        analyticContext.makeModification(this::class)
    }

    override fun visitDSTNumberLiteral(node: DSTNumberLiteral) {
        if (!isMethod) return
        if (isInnerClass) return
        if (node.parent == null) return
        if (!config.extreme && !random.randomBoolean()) return
        val name = DSTSyntheticName("syn" + "X" + syntheticVariableCounter.getAndIncrement() + node.typeBinding.name, node.typeBinding)
        val field = DSTSyntheticField(name.clone(), typeFactory.produceTypeFromBinding(node.typeBinding), node.clone())

        node.parent!!.replace(node, name)
        val body = currentType.bodyDeclarations.toMutableList()
        body.add(0, field)
        currentType.bodyDeclarations = body
        analyticContext.makeModification(this::class)
    }

    override fun visitDSTStringLiteral(node: DSTStringLiteral) {
        if (!isMethod) return
        if (isInnerClass) return
        if (node.parent == null) return
        if (!config.extreme && !random.randomBoolean()) return
        val name = DSTSyntheticName("syn" + "X" + syntheticVariableCounter.getAndIncrement() + node.typeBinding.name, node.typeBinding)
        val field = DSTSyntheticField(name.clone(), typeFactory.produceTypeFromBinding(node.typeBinding), node.clone())

        node.parent!!.replace(node, name)
        val body = currentType.bodyDeclarations.toMutableList()
        body.add(0, field)
        currentType.bodyDeclarations = body
        analyticContext.makeModification(this::class)
    }

    override fun visitDSTTypeLiteral(node: DSTTypeLiteral) {
        if (!isMethod) return
        if (isInnerClass) return
        if (node.parent == null) return
        if (!config.extreme && !random.randomBoolean()) return
        val name = DSTSyntheticName("syn" + "X" + syntheticVariableCounter.getAndIncrement() + node.typeBinding.name, node.typeBinding)
        val field = DSTSyntheticField(name.clone(), typeFactory.produceTypeFromBinding(node.typeBinding), node.clone())

        node.parent!!.replace(node, name)
        val body = currentType.bodyDeclarations.toMutableList()
        body.add(0, field)
        currentType.bodyDeclarations = body
        analyticContext.makeModification(this::class)
    }
}