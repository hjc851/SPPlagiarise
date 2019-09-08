package spplagiarise.obfuscation

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.inject
import spplagiarise.obfuscation.l2.DeclarationTypeNameQualifierDequalifier
import spplagiarise.obfuscation.l3.*
import spplagiarise.obfuscation.l4.BlockExtractor
import spplagiarise.obfuscation.l5.CombinedAssignmentExpanderFilter
import spplagiarise.obfuscation.l5.ForToWhileLoopFilter
import spplagiarise.obfuscation.l5.IncDecExpanderFilter
import spplagiarise.obfuscation.l5.ReplaceConstantWithVariableFilter
import spplagiarise.obfuscation.l6.AddBracketsToExpressionFilter
import spplagiarise.obfuscation.l6.ReplaceStaticFieldWithStaticImportFilter
import spplagiarise.obfuscation.l6.ReplaceStaticMethodWithStaticImportFilter
import spplagiarise.obfuscation.util.SwitchToIfFilter
import spplagiarise.util.IRandomGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DSTObfuscatorFilterFactory {

    @Inject
    private lateinit var random: IRandomGenerator

    @Inject
    private lateinit var analyticContext: IAnalyticContext

    fun produceObfuscators(): List<DSTObfuscatorFilter> {
        val filters = mutableListOf<DSTObfuscatorFilter>()
        fun randAdd(producer: () -> DSTObfuscatorFilter) {
            if (random.randomBoolean()) {
                val filter = producer()
                analyticContext.usingFilter(filter::class)
                filters.add(filter)
            }
        }

        //  L2
        randAdd { inject<DeclarationTypeNameQualifierDequalifier>() }

        //  L3
        randAdd { inject<ChangeAccessModifiers>() }
        randAdd { inject<MoveKnownFieldAssignmentToInitialiser>() }
        randAdd { inject<DeclareRedundantConstants>() }
        randAdd { inject<SynchroniserObfuscator>() }
//        randAdd { inject<VariableDeclarationAssignmentBreaker>() }
        randAdd { inject<VariableDeclarationAssignDefaultValue>() }
        randAdd { inject<StandardiseVariableDeclarationsAtMethodStart>() }
//        randAdd { inject<GroupMultipleVariableDeclarationsOfSameType>() }
        randAdd { inject<RearrangeMemberDeclarations>() }

        //  L4
        randAdd { inject<BlockExtractor>() }
        randAdd { inject<SwitchToIfFilter>() }

//        //  L5
        randAdd { inject<CombinedAssignmentExpanderFilter>() }
        randAdd { inject<ForToWhileLoopFilter>() }
        randAdd { inject<IncDecExpanderFilter>() }
        randAdd { inject<ReplaceConstantWithVariableFilter>() }
        randAdd { inject<ReplaceStaticFieldWithStaticImportFilter>() }
        randAdd { inject<ReplaceStaticMethodWithStaticImportFilter>() }
        randAdd { inject<AddBracketsToExpressionFilter>() }

        return filters
    }
}