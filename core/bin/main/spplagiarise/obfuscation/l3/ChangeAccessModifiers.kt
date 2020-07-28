package spplagiarise.obfuscation.l3

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import spplagiarise.util.searchOverridenMethod
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangeAccessModifiers : DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator
    
    @Inject
    private lateinit var config: Configuration

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        if (node.accessModifier != AccessModifier.PUBLIC) {
            node.accessModifier = AccessModifier.PUBLIC
            analyticContext.makeModification(this::class)
        }
    }

    override fun visitDSTEnumTypeDeclaration(node: DSTEnumTypeDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        if (node.accessModifier != AccessModifier.PUBLIC) {
            node.accessModifier = AccessModifier.PUBLIC
            analyticContext.makeModification(this::class)
        }
    }

    override fun visitDSTAnnotationTypeDeclaration(node: DSTAnnotationTypeDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        if (node.accessModifier != AccessModifier.PUBLIC) {
            node.accessModifier = AccessModifier.PUBLIC
            analyticContext.makeModification(this::class)
        }
    }

    override fun visitDSTFieldGroup(node: DSTFieldGroup) {
        if (!config.extreme && !random.randomBoolean()) return
        if (node.accessModifier != AccessModifier.PUBLIC) {
            node.accessModifier = AccessModifier.PUBLIC
            analyticContext.makeModification(this::class)
        }
    }

    override fun visitDSTConstructorDeclaration(node: DSTConstructorDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        if (node.accessModifier != AccessModifier.PUBLIC) {
            node.accessModifier = AccessModifier.PUBLIC
            analyticContext.makeModification(this::class)
        }
    }

    override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        if (node.accessModifier != AccessModifier.PUBLIC && node.binding.searchOverridenMethod() == null && !node.modifiers.isAbstract) {
            node.accessModifier = AccessModifier.PUBLIC
            analyticContext.makeModification(this::class)
        }
    }
}