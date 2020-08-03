package spplagiarise.obfuscation.util

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.ast.KnownTypeLibrary
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.dst.visitor.walk
import spplagiarise.inject
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

val nameIdCounter = AtomicInteger(0)

class BlockExtractor : DSTObfuscatorFilter {

    @Inject
    private lateinit var knownTypes: KnownTypeLibrary

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var config: Configuration

    lateinit var currentType: DSTTypeDeclaration
    lateinit var currentMethod: DSTMethodDeclaration

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        currentType = node
    }

    override fun visitDSTEnumTypeDeclaration(node: DSTEnumTypeDeclaration) {
        currentType = node
    }

    override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
        currentMethod = node
    }

    override fun visitDSTIfStatement(node: DSTIfStatement) {
        if (!::currentMethod.isInitialized) return

        val thenBody = node.thenStatement
        if (thenBody is DSTBlockStatement) {
            if (!config.extreme && !random.randomBoolean()) return
            // Validate that the body can be extracted
            val visitor = inject<BlockExtractorValidator>()
            thenBody.walk(visitor)

            if (visitor.blockCanBeExtracted()) {
                val parameters = visitor.refactoredParameters()
                extractBlock(thenBody, parameters) { call -> node.thenStatement = call }
            }
        }

        val elseBody = node.elseStatement
        if (elseBody is DSTBlockStatement) {
            // Validate that the body can be extracted
            val visitor = inject<BlockExtractorValidator>()
            elseBody.walk(visitor)

            if (visitor.blockCanBeExtracted()) {
                val parameters = visitor.refactoredParameters()
                extractBlock(elseBody, parameters) { call -> node.elseStatement = call }
            }
        }
    }

    override fun visitDSTWhileStatement(node: DSTWhileStatement) {
        if (!::currentMethod.isInitialized) return

        val body = node.body
        if (body is DSTBlockStatement) {
            if (!config.extreme && !random.randomBoolean()) return

            // Validate that the body can be extracted
            val visitor = inject<BlockExtractorValidator>()
            body.walk(visitor)

            if (visitor.blockCanBeExtracted()) {
                val parameters = visitor.refactoredParameters()
                extractBlock(body, parameters) { call -> node.body = call}
            }
        }
    }

    override fun visitDSTDoWhileStatement(node: DSTDoWhileStatement) {
        if (!::currentMethod.isInitialized) return

        val body = node.body
        if (body is DSTBlockStatement) {
            if (!config.extreme && !random.randomBoolean()) return

            // Validate that the body can be extracted
            val visitor = inject<BlockExtractorValidator>()
            body.walk(visitor)

            if (visitor.blockCanBeExtracted()) {
                val parameters = visitor.refactoredParameters()
                extractBlock(body, parameters) { call -> node.body = call}
            }
        }
    }

    override fun visitDSTForEachStatement(node: DSTForEachStatement) {
        if (!::currentMethod.isInitialized) return

        val body = node.body
        if (body is DSTBlockStatement) {
            if (!config.extreme && !random.randomBoolean()) return

            // Validate that the body can be extracted
            val visitor = inject<BlockExtractorValidator>()
            body.walk(visitor)

            if (visitor.blockCanBeExtracted()) {
                val parameters = visitor.refactoredParameters()
                extractBlock(body, parameters) { call -> node.body = call}
            }
        }
    }

    override fun visitDSTForStatement(node: DSTForStatement) {
        if (!::currentMethod.isInitialized) return

        val body = node.body
        if (body is DSTBlockStatement) {
            if (!config.extreme && !random.randomBoolean()) return

            // Validate that the body can be extracted
            val visitor = inject<BlockExtractorValidator>()
            body.walk(visitor)

            if (visitor.blockCanBeExtracted()) {
                val parameters = visitor.refactoredParameters()
                extractBlock(body, parameters) { call -> node.body = call}
            }
        }
    }

    private fun extractBlock (
            block: DSTBlockStatement,
            parameters: List<DSTParameter>,
            blockReplacement: (DSTSyntheticMethodCall) -> Unit
    ) {
        val methodDeclaration = DSTSyntheticMethodDeclaration(
                AccessModifier.PRIVATE,
                currentMethod.modifiers,
                "void",
                emptyList(),
                "synx${nameIdCounter.getAndIncrement()}",
                parameters,
                currentMethod.throws.map { it.clone() },
                block
        )

        val methodCall = DSTSyntheticMethodCall(methodDeclaration.name, parameters.map { it.name })

        blockReplacement(methodCall)
        (currentMethod.parent as DSTTypeDeclaration).bodyDeclarations += methodDeclaration
        analyticContext.makeModification(this::class)
    }
}

