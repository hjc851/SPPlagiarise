package spplagiarise.obfuscation.filters

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.dst.*
import spplagiarise.obfuscation.DSTObfuscatorFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupMultipleVariableDeclarationsOfSameTypeFilter :
    DSTObfuscatorFilter {

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    @Inject
    private lateinit var random: IRandomGenerator
    
    @Inject
    private lateinit var config: Configuration

    override fun visitDSTBlockStatement(node: DSTBlockStatement) {
        if (!config.extreme && !random.randomBoolean()) return

        val decls = node.statements
                .filterIsInstance<DSTLocalVariableDeclarationGroup>()

        val statementsCopy = node.statements
                .filterNot { it is DSTLocalVariableDeclarationGroup }
                .toMutableList()

        val groups = decls.groupBy(
                { it.type.binding },
                { it.type to it.variables }
        ).map { it.value.first().first to it.value.flatMap { it.second } }
                .map { DSTLocalVariableDeclarationGroup(it.second, it.first) }

        for (group in groups.reversed()) {
            statementsCopy.add(0, group)

            // 1 modification per joined field, minus 1 for the original
            analyticContext.makeModification(this::class, group.variables.size - 1)
        }

        node.statements = statementsCopy
        statementsCopy.forEach { it.parent = node }
    }

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        if (!config.extreme && !random.randomBoolean()) return
        val fields = node.bodyDeclarations.filterIsInstance<DSTFieldGroup>()

        val declCopy = node.bodyDeclarations
                .filterNot { it is DSTFieldGroup }
                .toMutableList()

        val dGroups = fields.groupBy(
                { it.toDescriptor() },
                { it }
        )

        val groups = dGroups.map { it.value.first() to it.value.flatMap { it.fields } }
                .map {
                    DSTFieldGroup(
                            it.first.accessModifier,
                            it.first.modifiers,
                            it.first.type,
                            it.second
                    )
                }

        for (group in groups.reversed()) {
            declCopy.add(0, group)

            // 1 modification per joined field, minus 1 for the original
            analyticContext.makeModification(this::class, group.fields.size - 1)
        }

        node.bodyDeclarations = declCopy
    }

    fun DSTFieldGroup.toDescriptor(): String {
        val arr = ByteArray(9) { 0 }
        if (this.accessModifier == AccessModifier.PUBLIC) arr[0] = 1
        if (this.accessModifier == AccessModifier.PRIVATE) arr[1] = 1
        if (this.accessModifier == AccessModifier.PACKAGEPROTECTED) arr[2] = 1
        if (this.accessModifier == AccessModifier.PROTECTED) arr[3] = 1
        if (this.modifiers.isFinal) arr[4] = 1
        if (this.modifiers.isStatic) arr[5] = 1
        if (this.modifiers.isStrict) arr[6] = 1
        if (this.modifiers.isTransient) arr[7] = 1
        if (this.modifiers.isVolatile) arr[8] = 1
        return arr.joinToString("") + this.type.binding.qualifiedName
    }
}