package spplagiarise.analytics

import spplagiarise.cdi.ObfuscatorScoped
import spplagiarise.document.AnalyticsDocument
import spplagiarise.obfuscation.DSTObfuscatorFilter
import javax.annotation.PreDestroy
import kotlin.reflect.KClass

interface IAnalyticContext {
    fun usingFilter(kls: KClass<out DSTObfuscatorFilter>)
    fun makeModification(kls: KClass<out DSTObfuscatorFilter>, count: Int = 1)
    fun toDocument(): AnalyticsDocument
}

@ObfuscatorScoped
class AnalyticContext : IAnalyticContext {

    private val stores = mutableMapOf<KClass<out DSTObfuscatorFilter>, AnalyticStore>()

    override fun usingFilter(kls: KClass<out DSTObfuscatorFilter>) {
        stores[kls] = AnalyticStore()
    }

    override fun makeModification(kls: KClass<out DSTObfuscatorFilter>, count: Int) {
        val store = stores[kls]!!
        store.modCount += count
    }

    override fun toDocument(): AnalyticsDocument {
        val mods = stores.map { it.key.toString() to it.value.modCount }.toMap()
        return AnalyticsDocument(mods)
    }

    private class AnalyticStore {
        var modCount: Int = 0
    }
}
