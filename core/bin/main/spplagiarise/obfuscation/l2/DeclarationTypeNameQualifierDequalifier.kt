package spplagiarise.obfuscation.l2

import org.eclipse.jdt.core.dom.ITypeBinding
import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.dst.visitor.DSTNodeVisitor
import spplagiarise.dst.visitor.evaluate
import spplagiarise.dst.visitor.walk
import spplagiarise.naming.DeferredNameContext
import spplagiarise.naming.DeferredNameMappingContext
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.obfuscation.util.TypeRewriteStrategyType
import spplagiarise.util.IRandomGenerator
import spplagiarise.util.searchOverridenMethod
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeclarationTypeNameQualifierDequalifier : DSTObfuscatorFilter {

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

    private lateinit var cu: DSTCompilationUnit

    override fun visitDSTCompilationUnit(node: DSTCompilationUnit) {
        cu = node
    }

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        if (node.extends != null) {
            node.extends = rewrite(node.extends!!)
            node.implements = node.implements.map { rewrite(it) }
        }
    }

    override fun visitDSTEnumTypeDeclaration(node: DSTEnumTypeDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        node.implements = node.implements.map { rewrite(it) }
    }

    override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        if (node.binding.searchOverridenMethod() == null) {
            node.returnType = rewrite(node.returnType)
            node.parameters.forEach { it.type = rewrite(it.type) }
        }
    }

    //  Target points

    override fun visitDSTFieldGroup(node: DSTFieldGroup) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type = rewrite(node.type)
    }

    override fun visitDSTLocalVariableDeclarationGroup(node: DSTLocalVariableDeclarationGroup) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type = rewrite(node.type)
    }

    override fun visitDSTSingleVariableDeclaration(node: DSTSingleVariableDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type = rewrite(node.type)
    }

    override fun visitDSTAnnotationMember(node: DSTAnnotationMember) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type = rewrite(node.type)
    }

    override fun visitDSTCastExpression(node: DSTCastExpression) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type = rewrite(node.type)
    }

    override fun visitDSTInstanceOfExpression(node: DSTInstanceOfExpression) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type = rewrite(node.type)
    }

    override fun visitDSTTypeLiteral(node: DSTTypeLiteral) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type = rewrite(node.type)
    }

    override fun visitDSTConstructorCall(node: DSTConstructorCall) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type = rewrite(node.type)
    }

    override fun visitDSTArrayCreation(node: DSTArrayCreation) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type.typeName = rewrite(node.type.typeName)
    }

    override fun visitDSTCreationRefExpression(node: DSTCreationRefExpression) {
        if (!config.extreme && !random.randomBoolean()) return
        node.type = rewrite(node.type)
    }

    //  Special cases -> looking at the scope or qualifier

    override fun visitDSTMethodCall(node: DSTMethodCall) {
        if (!config.extreme && !random.randomBoolean()) return
        val expression = node.expression
        if (expression is DSTName && expression.binding is ITypeBinding)
            node.expression = rewrite(expression)
    }

    override fun visitDSTSuperMethodCall(node: DSTSuperMethodCall) {
        if (!config.extreme && !random.randomBoolean()) return
        val qualifier = node.qualifier
        if (qualifier != null && qualifier.binding is ITypeBinding)
            node.qualifier = rewrite(qualifier)
    }

    override fun visitDSTSuperConstructorCall(node: DSTSuperConstructorCall) {
        if (!config.extreme && !random.randomBoolean()) return
        val qualifier = node.qualifier
        if (qualifier != null && qualifier is DSTName && qualifier.binding is ITypeBinding)
            node.qualifier = rewrite(qualifier)
    }

    override fun visitDSTThisExpression(node: DSTThisExpression) {
        if (!config.extreme && !random.randomBoolean()) return
        val qualifier = node.qualifier
        if (qualifier != null && qualifier.binding is ITypeBinding)
            node.qualifier = rewrite(qualifier)
    }

    //  Rewriting logic

    private fun rewrite(type: DSTType): DSTType {
        val strategyType = type.evaluate(TypeRewriteStrategyType.Visitor)

        if (strategyType == TypeRewriteStrategyType.QUALIFY && !validateQualification(type.binding, type))
            return type

        if (strategyType == TypeRewriteStrategyType.SIMPLIFY && !validateSimplification(type.binding, type))
            return type

        val strategyFactory = strategyType.strategyFactory
        analyticContext.makeModification(this::class)
        return type.evaluate(strategyFactory(nameContext), cu)
    }

    private fun rewrite(name: DSTName): DSTName {
        if (name.binding !is ITypeBinding)
            return name

        val strategyType = name.evaluate(TypeRewriteStrategyType.Visitor)

        if (strategyType == TypeRewriteStrategyType.QUALIFY && !validateQualification(name.binding as ITypeBinding, name))
            return name

        if (strategyType == TypeRewriteStrategyType.SIMPLIFY && !validateSimplification(name.binding as ITypeBinding, name))
            return name

        val strategyFactory = strategyType.strategyFactory
        analyticContext.makeModification(this::class)
        return name.evaluate(strategyFactory(nameContext), cu)
    }

    private fun validateSimplification(binding: ITypeBinding, node: DSTNode): Boolean {
        val lastNameComponent = binding.qualifiedName.split(".")
                .last()

        val mem = object : Any() {
            var foundIdentifier: Boolean = false
        }

        val cu = node.findParent<DSTCompilationUnit>()!!
        cu.walk(object : DSTNodeVisitor {
            override fun visitDSTParameter(node: DSTParameter) {
                val name = node.name
                if (name is DSTDeferredSimpleName) {
                    val nameStr = mappingContext.getMappedName(name.id, false)
                    if (nameStr == lastNameComponent)
                        mem.foundIdentifier = true
                } else if (name is DSTKnownSimpleName) {
                    if (name.name == lastNameComponent)
                        mem.foundIdentifier = true
                }
            }

            override fun visitDSTLocalVariableDeclaration(node: DSTLocalVariableDeclaration) {
                val name = node.name
                if (name is DSTDeferredSimpleName) {
                    val name = mappingContext.getMappedName(name.id, false)
                    if (name == lastNameComponent)
                        mem.foundIdentifier = true
                }
            }

            override fun visitDSTSingleVariableDeclaration(node: DSTSingleVariableDeclaration) {
                val name = mappingContext.getMappedName(node.name.id, false)
                if (name == lastNameComponent)
                    mem.foundIdentifier = true
            }

            override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
                val name = mappingContext.getMappedName(node.name.id, true)
                if (name == lastNameComponent)
                    mem.foundIdentifier = true
            }

            override fun visitDSTFieldDeclaration(node: DSTFieldDeclaration) {
                val name = node.name
                if (name is DSTDeferredSimpleName) {
                    val name = mappingContext.getMappedName(name.id, false)
                    if (name == lastNameComponent)
                        mem.foundIdentifier = true
                }
            }

            override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
                if (node.name is DSTDeferredSimpleName) {
                    val name = mappingContext.getMappedName((node.name as DSTDeferredSimpleName).id, false)
                    if (name == lastNameComponent)
                        mem.foundIdentifier = true
                } else {
                    val name = (node.name as DSTKnownSimpleName).name
                    if (name == lastNameComponent)
                        mem.foundIdentifier = true
                }
            }
        })

        return !mem.foundIdentifier
    }

    private fun validateQualification(binding: ITypeBinding, node: DSTNode): Boolean {
        // Cannot qualify the default package or primitives
        if (binding.`package` == null || binding.`package`.name == "")
            return false

        val firstNameComponent = binding.qualifiedName.split(".")
                .first()

        // Start searching at the first block parent (could be the method)
        var currentNode: DSTNode? = node.findParent<DSTBlockStatement>()
        while (currentNode != null) {
            when (currentNode) {
                is DSTBlockStatement -> {
                    for (statement in currentNode.statements) {
                        when (statement) {
                            is DSTLocalVariableDeclarationGroup -> {
                                for (variable in statement.variables) {
                                    val vName = variable.name
                                    if (vName is DSTDeferredSimpleName) {
                                        val name = mappingContext.getMappedName(vName.id, false)
                                        if (name == firstNameComponent)
                                            return false
                                    }
                                }
                            }
                        }
                    }
                }

                is DSTForStatement -> {
                    for (it in currentNode.initialisers) {
                        if (it is DSTVariableDeclarationExpression) {
                            for (variable in it.variables) {
                                val vName = variable.name
                                if (vName is DSTDeferredSimpleName) {
                                    val name = mappingContext.getMappedName(vName.id, false)
                                    if (name == firstNameComponent)
                                        return false
                                }
                            }
                        }
                    }
                }

                is DSTForEachStatement -> {
                    val name = mappingContext.getMappedName(currentNode.variable.name.id, false)
                    if (name == firstNameComponent)
                        return false
                }

                is DSTCatchClause -> {
                    val name = mappingContext.getMappedName(currentNode.exception.name.id, false)
                    if (name == firstNameComponent)
                        return false
                }

                is DSTTryStatement -> {
                    for (resource in currentNode.resources) {
                        if (resource is DSTVariableDeclarationExpression) {
                            for (variable in resource.variables) {
                                val vName = variable.name
                                if (vName is DSTDeferredSimpleName) {
                                    val name = mappingContext.getMappedName(vName.id, false)
                                    if (name == firstNameComponent)
                                        return false
                                }
                            }
                        }
                    }
                }

                is DSTMethodDeclaration -> {
                    val name = if (currentNode.name is DSTDeferredSimpleName)
                        mappingContext.getMappedName((currentNode.name as DSTDeferredSimpleName).id, false)
                    else
                        (currentNode.name as DSTKnownSimpleName).name

                    if (name == firstNameComponent)
                        return false

                    for (parameter in currentNode.parameters) {
//                        val pname = mappingContext.getMappedName(parameter.name.id, false)
//                        if (pname == firstNameComponent)
//                            return false

                        val pname = parameter.name
                        if (pname is DSTDeferredSimpleName) {
                            val nameStr = mappingContext.getMappedName(pname.id, false)
                            if (nameStr == firstNameComponent)
                                return false

                        } else if (pname is DSTKnownSimpleName) {
                            if (pname.name == firstNameComponent)
                                return false
                        }
                    }
                }

                is DSTLambdaExpression -> {
                    TODO()
                }

                is DSTTypeDeclaration -> {
                    val name = mappingContext.getMappedName(currentNode.name.id, true)
                    if (name == firstNameComponent)
                        return false

                    if (currentNode is DSTClassOrInterfaceTypeDeclaration) {
                        for (declaration in currentNode.bodyDeclarations) {
                            when (declaration) {
                                is DSTMethodDeclaration -> {
                                    val name = if (declaration.name is DSTDeferredSimpleName)
                                        mappingContext.getMappedName((declaration.name as DSTDeferredSimpleName).id, false)
                                    else
                                        (declaration.name as DSTKnownSimpleName).name

                                    if (name == firstNameComponent)
                                        return false

                                    for (parameter in declaration.parameters) {
//                                        val pname = mappingContext.getMappedName(parameter.name.id, false)
//                                        if (pname == firstNameComponent)
//                                            return false

                                        val pname = parameter.name
                                        if (pname is DSTDeferredSimpleName) {
                                            val nameStr = mappingContext.getMappedName(pname.id, false)
                                            if (nameStr == firstNameComponent)
                                                return false

                                        } else if (pname is DSTKnownSimpleName) {
                                            if (pname.name == firstNameComponent)
                                                return false
                                        }
                                    }
                                }

                                is DSTFieldGroup -> {
                                    for (field in declaration.fields) {
                                        val fName = field.name
                                        if (fName is DSTDeferredSimpleName) {
                                            val name = mappingContext.getMappedName(fName.id, false)
                                            if (name == firstNameComponent)
                                                return false
                                        }
                                    }
                                }

                                is DSTTypeDeclaration -> {
                                    val name = mappingContext.getMappedName(declaration.name.id, true)
                                    if (name == firstNameComponent)
                                        return false
                                }
                            }
                        }
                    }
                }
            }

            currentNode = currentNode.parent
        }

        return true
    }
}

inline fun <reified T> DSTNode.findParent(): T? {
    var currentNode: DSTNode? = this
    while (currentNode != null) {
        if (currentNode is T)
            return currentNode

        currentNode = currentNode.parent
    }
    return null
}