package spplagiarise.obfuscation

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.inject
import spplagiarise.obfuscation.filters.*
import spplagiarise.obfuscation.util.BlockExtractor
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

    @Inject
    private lateinit var config: Configuration

    fun produceObfuscators(): List<DSTObfuscatorFilter> {
        val filters = mutableListOf<DSTObfuscatorFilter>()
        fun randAdd(producer: () -> DSTObfuscatorFilter) {
            if (random.randomBoolean()) {
                val filter = producer()
                filters.add(filter)
            }
        }

        //  L2
        if (config.l2) {
            randAdd { inject<DeclarationTypeNameQualifierDequalifierFilter>() }
            randAdd { inject<ReplaceStaticFieldWithStaticImportFilter>() }
            randAdd { inject<ReplaceStaticMethodWithStaticImportFilter>() }
        }

        //  L3
       if (config.l3) {
           randAdd { inject<ChangeAccessModifiersFilter>() }
           randAdd { inject<MoveKnownFieldAssignmentToInitialiserFilter>() }
           randAdd { inject<DeclareRedundantConstantsFilter>() }
           randAdd { inject<SynchroniserObfuscatorFilter>() }
           randAdd { inject<VariableDeclarationAssignDefaultValueFilter>() }
           randAdd { inject<StandardiseVariableDeclarationsAtMethodStartFilter>() }
           randAdd { inject<RearrangeMemberDeclarationsFilter>() }
       }

        //  L4
        if (config.l4) {
            randAdd { inject<BlockExtractor>() }
        }

//        //  L5
        if (config.l5) {
            randAdd { inject<CombinedAssignmentExpanderFilter>() }
            randAdd { inject<ForToWhileLoopFilter>() }
            randAdd { inject<IncDecExpanderFilter>() }
            randAdd { inject<SwitchToIfFilter>() }
            randAdd { inject<ReplaceConstantWithVariableFilter>() }
            randAdd { inject<AddBracketsToExpressionFilter>() }
        }

        return filters
    }
}