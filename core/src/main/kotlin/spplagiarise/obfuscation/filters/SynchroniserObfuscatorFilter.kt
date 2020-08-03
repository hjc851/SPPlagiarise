package spplagiarise.obfuscation.filters

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.DSTMethodDeclaration
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SynchroniserObfuscatorFilter : DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        val body = node.body
        if (body != null) {
            analyticContext.makeModification(this::class)
            node.modifiers.isSynchronised = true
        }
    }
}