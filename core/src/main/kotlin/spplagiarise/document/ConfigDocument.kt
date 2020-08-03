package spplagiarise.document

class ConfigDocument (
        val input: String,
        val output: String,
        val libs: String,
        val copies: Int,
        val extreme: Boolean,
        val seed: Long?,
        val l1: Boolean,
        val l2: Boolean,
        val l3: Boolean,
        val l4: Boolean,
        val l5: Boolean,
        val randomWeight: Int
)

