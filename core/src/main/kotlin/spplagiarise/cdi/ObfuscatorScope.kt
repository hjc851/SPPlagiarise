package spplagiarise.cdi

import java.lang.annotation.Inherited
import java.util.function.Function
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.NormalScope
import javax.enterprise.context.spi.Context
import javax.enterprise.context.spi.Contextual
import javax.enterprise.context.spi.CreationalContext
import javax.enterprise.event.Observes
import javax.enterprise.inject.spi.*
import javax.inject.Singleton

@Inherited
@NormalScope(passivating = false)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class ObfuscatorScoped

@Singleton
class ObfuscatorExtension : Extension {

    fun beforeBeanDiscovery(@Observes bbd: BeforeBeanDiscovery) {
        bbd.addScope(ObfuscatorScoped::class.java, true, false)
    }

    fun <T> processAnnotatedType(@Observes pat: ProcessAnnotatedType<T>) {
        if (pat.annotatedType.javaClass == ObfuscatorContext::class.java)
            pat.veto()
    }

    fun afterBeanDiscovery(@Observes abd: AfterBeanDiscovery) {
        val ctx = ObfuscatorContext()

        abd.addBean<ObfuscatorContext>()
            .scope(ApplicationScoped::class.java)
            .types(ObfuscatorContext::class.java)
            .id("__obfctx")
            .createWith(Function { ctx })

        abd.addContext(ctx)
    }
}

open class ObfuscatorContext : Context {

    override fun isActive(): Boolean {
        val round = tl.get()
        return round != null
    }

    override fun getScope(): Class<out Annotation> = ObfuscatorScoped::class.java

    override fun <T : Any?> get(contextual: Contextual<T>, creationalContext: CreationalContext<T>): T {
        val bean = contextual as Bean<T>
        val instance = contextual.create(creationalContext)
        currentRound.setBean(bean.beanClass, instance as Any, contextual as Contextual<Any>, creationalContext as CreationalContext<Any>)
        return instance
    }

    override fun <T : Any?> get(contextual: Contextual<T>): T? {
        val bean = contextual as Bean<T>
        return currentRound.getBean(bean.beanClass) as T?
    }

    //  Contexts

    private val tl = InheritableThreadLocal<Round>()

    private val currentRound: Round
        get() = tl.get()

    open fun use(handle: () -> Unit) {
        tl.set(Round(0))
        handle()
        disposeRound()
        tl.remove()
    }

    private fun disposeRound() {
        val round = currentRound
        for ((type, ctx) in round.beans) {
            val (bean, creational, creationContext) = ctx

            creational.destroy(bean, creationContext)
        }
    }

    data class BeanContext(val bean: Any, val context: Contextual<Any>, val creationalContext: CreationalContext<Any>)

    class Round(val id: Int) {

        val beans = mutableMapOf<Class<*>, BeanContext>()

        fun getBean(kls: Class<*>): Any? {
            return beans[kls]?.bean
        }

        fun setBean(kls: Class<*>, bean: Any, context: Contextual<Any>, creationalContext: CreationalContext<Any>) {
            beans[kls] = BeanContext(bean, context, creationalContext)
        }

        fun beanTypes(): Set<Class<*>> = beans.keys
    }
}