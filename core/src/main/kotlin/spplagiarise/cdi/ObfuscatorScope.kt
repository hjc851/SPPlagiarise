package spplagiarise.cdi

import java.util.*
import javax.enterprise.context.Dependent
import javax.enterprise.context.spi.Context
import javax.enterprise.context.spi.Contextual
import javax.enterprise.context.spi.CreationalContext
import javax.enterprise.event.Observes
import javax.enterprise.inject.spi.*
import javax.inject.Inject
import javax.inject.Scope

@Scope
@Target(AnnotationTarget.CLASS)
annotation class ObfuscatorScoped

class ObfuscatorExtension : Extension {
    fun beforeBeanDiscovery(@Observes bbd: BeforeBeanDiscovery) {
        bbd.addScope(ObfuscatorScoped::class.java, true, false)
    }

    fun <T> processAnnotatedType(@Observes pat: ProcessAnnotatedType<T>) {
        Unit
    }

    fun afterBeanDiscovery(@Observes abd: AfterBeanDiscovery) {
        abd.addContext(ObfuscatorContext())
    }
}

@Dependent
class ObfuscatorContext : Context {

    @Inject
    private lateinit var bm: BeanManager

    private var active: Boolean = true
    override fun isActive(): Boolean {
        return active
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

    //  Using

    fun use(id: Int, handle: () -> Unit) {
        active = true
        pushRound(id)
        handle()
        popRound()
        active = false
    }

    //  Bean Management

    private val roundStack = Stack<Int>()
    private val rounds = mutableMapOf<Int, Round>()

    fun pushRound(id: Int) {
        roundStack.push(id)
        rounds[id] = Round(id)
    }

    fun popRound(): Int {
        val id = roundStack.pop()
        val round = rounds.remove(id)!!

        for ((type, ctx) in round.beans) {
            val (bean, creational, creationContext) = ctx

            creational.destroy(bean, creationContext)
        }

        return id
    }

    val currentRound: Round
        get() {
            return rounds[roundStack.peek()]!!
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