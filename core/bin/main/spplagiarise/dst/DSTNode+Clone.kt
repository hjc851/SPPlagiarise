package spplagiarise.dst

fun <T : DSTNode> T.clone(): T {
    val fields = this.javaClass.declaredFields
    val fieldTypes = fields.map { it.type }.toTypedArray()
    val constructor = this.javaClass.getConstructor(*fieldTypes)

    fields.forEach { it.isAccessible = true }
    constructor.isAccessible = true

    val fieldValues = fields.map { it.get(this) }

    val clones = fieldValues.map {
        when (it) {
            is DSTNode -> it.clone()
            is List<*> -> it.deepClone()
            else -> it
        }
    }

    try {
        val instance = constructor.newInstance(*clones.toTypedArray())
        return instance
    } catch (e: Exception) {
        throw e
    }
}

fun List<*>.deepClone(): List<*> {
    return this.map {
        when (it) {
            is DSTNode -> it.clone()
            is List<*> -> it.deepClone()
            else -> it
        }
    }
}



