package spplagiarise.obfuscation.l3

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.DSTClassOrInterfaceTypeDeclaration
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RearrangeMemberDeclarations : DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return

        node.bodyDeclarations = node.bodyDeclarations
                .shuffled()
        analyticContext.makeModification(this::class)
    }
}