package spplagiarise.dst

import java.util.List as JList

fun DSTNode.replace(node: DSTNode, with: DSTNode) {

    val fields = this::class.java.declaredFields

    for (field in fields) {
        field.isAccessible = true
        val value = field.get(this)

        if (value is DSTNode && value == node) {
            field.set(this, with)
            with.parent = this
            return

        } else if (value is JList<*>) {
            val index = value.indexOf(node)
            if (index >= 0) {
                val method = value::class.java.getMethod("set", Int::class.java, Object::class.java)
                method.isAccessible = true
                method.invoke(value, index, with)

                with.parent = this
                return
            }
        }
    }

    throw IllegalArgumentException("Cannot replace child ${node} of ${this} with ${with}")
}
