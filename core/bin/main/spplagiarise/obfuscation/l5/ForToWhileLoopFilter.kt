package spplagiarise.obfuscation.l5

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.ast.KnownTypeLibrary
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForToWhileLoopFilter: DSTObfuscatorFilter {

    @Inject
    private lateinit var knownTypes: KnownTypeLibrary

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    override fun visitDSTForStatement(node: DSTForStatement) {
        if (!config.extreme && !random.randomBoolean()) return
        val condition = node.condition ?: DSTBooleanLiteral(knownTypes.booleanType, true)

        val blockStatements = mutableListOf<DSTStatement>()
        blockStatements.addAll(node.initialisers.map { DSTExpressionStatement(it) })
        blockStatements.add(DSTWhileStatement(
                condition, DSTBlockStatement(mutableListOf<DSTStatement>(
                DSTBlockStatement(listOf(node.body))
            ).apply { node.updaters.forEach { this.add(DSTExpressionStatement(it)) } } )
        ))

        val block = DSTBlockStatement(blockStatements)
        node.parent!!.replace(node, block)
        analyticContext.makeModification(this::class)
    }
}

