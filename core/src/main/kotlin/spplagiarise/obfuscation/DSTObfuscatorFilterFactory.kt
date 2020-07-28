package spplagiarise.obfuscation

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
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

    @Inject
    private lateinit var config: Configuration

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
        if (config.l2) {
            randAdd { inject<DeclarationTypeNameQualifierDequalifier>() }
            randAdd { inject<ReplaceStaticFieldWithStaticImportFilter>() }
            randAdd { inject<ReplaceStaticMethodWithStaticImportFilter>() }
        }

        //  L3
       if (config.l3) {
           randAdd { inject<ChangeAccessModifiers>() }
           randAdd { inject<MoveKnownFieldAssignmentToInitialiser>() }
           randAdd { inject<DeclareRedundantConstants>() }
           randAdd { inject<SynchroniserObfuscator>() }
           randAdd { inject<VariableDeclarationAssignDefaultValue>() }
           randAdd { inject<StandardiseVariableDeclarationsAtMethodStart>() }
           randAdd { inject<RearrangeMemberDeclarations>() }
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