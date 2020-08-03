package spplagiarise.analytics

import spplagiarise.cdi.ObfuscatorScoped
import spplagiarise.document.AnalyticsDocument
import spplagiarise.obfuscation.DSTObfuscatorFilter
import javax.annotation.PreDestroy
import kotlin.reflect.KClass

interface IAnalyticContext {
//    fun usingFilter(kls: KClass<out DSTObfuscatorFilter>)
    fun makeModification(kls: KClass<out DSTObfuscatorFilter>, count: Int = 1)
    fun setIdentifierMappingCount(mappingCount: Int)
    fun toDocument(): AnalyticsDocument
}

@ObfuscatorScoped
open class AnalyticContext : IAnalyticContext {

    private var identifierMappingCount: Int = 0
    private val stores = mutableMapOf<KClass<out DSTObfuscatorFilter>, AnalyticStore>()

    @PreDestroy
    open fun close() {}

//    override fun usingFilter(kls: KClass<out DSTObfuscatorFilter>) {
//        stores[kls] = AnalyticStore()
//    }

    override fun makeModification(kls: KClass<out DSTObfuscatorFilter>, count: Int) {
        val store = stores.getOrPut(kls) {
            AnalyticStore()
        }

        store.modCount += count
    }

    override fun setIdentifierMappingCount(mappingCount: Int) {
        this.identifierMappingCount = mappingCount
    }

    override fun toDocument(): AnalyticsDocument {
        val mods = stores.map { it.key.toString() to it.value.modCount }.toMap().toMutableMap()
        mods["identifiers"] = identifierMappingCount
        return AnalyticsDocument(mods)
    }

    private class AnalyticStore {
        var modCount: Int = 0
    }
}
